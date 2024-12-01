package account;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.authorization.event.AuthorizationEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AuthEventListener {

    private final AppUserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public AuthEventListener(AppUserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void onAbstractAuthenticationEvent (AbstractAuthenticationEvent event) {
        LocalDateTime now = LocalDateTime.now();
        if (event instanceof AuthenticationSuccessEvent) {
            AppUserAdapter adapter = (AppUserAdapter) event.getAuthentication().getPrincipal();
            AppUser user = adapter.getAppUser();
            user.setFailedAttempts(0);
            userRepository.save(user);
        } else if (event instanceof AbstractAuthenticationFailureEvent) {
            String email = ((AbstractAuthenticationFailureEvent) event).getAuthentication().getName();
            AppUser user = userRepository.findByEmail(email).orElse(null);
            if (null != user && !user.isAccountNonLocked()) {
                return;
            } // Don't log attempts by locked accounts.
            SecurityEvent loginFailedEvent = new SecurityEvent();
            loginFailedEvent.setDate(now);
            loginFailedEvent.setSubject(email);
            loginFailedEvent.setUser(user);
            loginFailedEvent.setAction("LOGIN_FAILED");
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            loginFailedEvent.setObject(request.getRequestURI());
            loginFailedEvent.setPath(request.getRequestURI());
            eventPublisher.publishEvent(loginFailedEvent);
            if (null == loginFailedEvent.getUser()) {
                return;
            }
            user.incrementFailedAttempts();
            userRepository.save(user);
            if (user.getFailedAttempts() >= 5) {
                LocalDateTime now2 = LocalDateTime.now();
                SecurityEvent bruteForceEvent = new SecurityEvent();
                bruteForceEvent.setDate(now2);
                bruteForceEvent.setUser(user);
                bruteForceEvent.setSubject(user.getEmail().toLowerCase());
                bruteForceEvent.setAction("BRUTE_FORCE");
                bruteForceEvent.setObject(request.getRequestURI());
                bruteForceEvent.setPath(request.getRequestURI());
                eventPublisher.publishEvent(bruteForceEvent);
                for (UserRole role : user.getRoles()) {
                    if (role.getName().equals("ROLE_ADMINISTRATOR")) {
                        return;
                    }
                }
                user.setAccountNonLocked(false);
                userRepository.save(user);
                LocalDateTime now3 = LocalDateTime.now();
                SecurityEvent userLockedEvent = new SecurityEvent();
                userLockedEvent.setDate(now3);
                userLockedEvent.setUser(user);
                userLockedEvent.setSubject(user.getEmail().toLowerCase());
                userLockedEvent.setAction("LOCK_USER");
                userLockedEvent.setObject("Lock user " + user.getEmail());
                userLockedEvent.setPath(request.getRequestURI());
                eventPublisher.publishEvent(userLockedEvent);
            }
        }
    }

    @EventListener
    public void onAuthorizationEvent (AuthorizationEvent event) {
        if (event instanceof AuthorizationDeniedEvent) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            SecurityEvent accessDeniedEvent = new SecurityEvent();
            accessDeniedEvent.setDate(LocalDateTime.now());
            accessDeniedEvent.setAction("ACCESS_DENIED");
            accessDeniedEvent.setPath(request.getRequestURI());
            accessDeniedEvent.setObject(request.getRequestURI());
            if (event.getAuthentication().get().getPrincipal() instanceof AppUserAdapter) {
                AppUserAdapter adapter = (AppUserAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                AppUser user = adapter.getAppUser();
                accessDeniedEvent.setUser(user);
                accessDeniedEvent.setSubject(user.getEmail().toLowerCase());
                eventPublisher.publishEvent(accessDeniedEvent);
            }
        }
    }


}
