package account;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServiceAuth {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final ApplicationEventPublisher eventPublisher;

    private boolean beforeFirstUser;
    private final String[] breachedPasswords = {"PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"};

    @Autowired
    public ServiceAuth(AppUserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder encoder, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.beforeFirstUser = !userRepository.exists();
        this.eventPublisher = eventPublisher;
    }

    public AppUserDTO createUser(RegistrationRequestDTO request, HttpServletRequest httpServletRequest) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserExistException();
        } else {
            if (request.getPassword().length() < 12) {
                throw new PasswordLengthException();
            }
            for (String breachedPassword : breachedPasswords) {
                if (request.getPassword().equals(breachedPassword)) {
                    throw new PasswordBreachedException();
                }
            }
            AppUser user = new AppUser();
            user.setName(request.getName());
            user.setLastname(request.getLastname());
            user.setEmail(request.getEmail());
            user.setPassword(encoder.encode(request.getPassword()));
            if (beforeFirstUser) {
                user.getRoles().add(roleRepository.findByName("ROLE_ADMINISTRATOR").orElseThrow(() -> new NotFoundException("Role not found!")));
                beforeFirstUser = false;
            } else {
                user.getRoles().add(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new NotFoundException("Role not found!")));
            }
            userRepository.save(user);
            SecurityEvent userCreateEvent = new SecurityEvent();
            userCreateEvent.setDate(LocalDateTime.now());
            userCreateEvent.setAction("CREATE_USER");
            userCreateEvent.setUser(user);
            userCreateEvent.setSubject("Anonymous");
            userCreateEvent.setObject(user.getEmail().toLowerCase());
            userCreateEvent.setPath("api/auth/signup");
            eventPublisher.publishEvent(userCreateEvent);
            return user.createDTO();
        }
    }

    public PasswordUpdateSuccessDTO changePassword(AppUserAdapter adapter, PassChangeRequestDTO request) {
        AppUser user = adapter.getAppUser();
        String newPassword = request.getNewPass();
        if (encoder.matches(newPassword, user.getPassword())) {
            throw new PasswordUnchangedException();
        }
        if (newPassword.length() < 12) {
            throw new PasswordLengthException();
        }
        for (String breachedPassword : breachedPasswords) {
            if (newPassword.equals(breachedPassword)) {
                throw new PasswordBreachedException();
            }
        }
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        SecurityEvent passChangeEvent = new SecurityEvent();
        passChangeEvent.setDate(LocalDateTime.now());
        passChangeEvent.setAction("CHANGE_PASSWORD");
        passChangeEvent.setUser(user);
        passChangeEvent.setSubject(user.getEmail().toLowerCase());
        passChangeEvent.setObject(user.getEmail().toLowerCase());
        passChangeEvent.setPath("api/auth/changepass");
        eventPublisher.publishEvent(passChangeEvent);
        return new PasswordUpdateSuccessDTO(adapter.getEmail());
    }
}
