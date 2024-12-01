package account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        AppUserAdapter adapter = (AppUserAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = adapter.getAppUser();
        System.out.println(user.getEmail());
        for (UserRole role : user.getRoles()) {
            System.out.println(role.getName());
        }
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("Access Denied!");
        errorDTO.setPath(request.getRequestURI());
        errorDTO.setError("Forbidden");
        errorDTO.setStatus(HttpStatus.FORBIDDEN.value());
        errorDTO.setTimestamp(LocalDateTime.now());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        response.getOutputStream().println(mapper.writeValueAsString(errorDTO));
    }
}
