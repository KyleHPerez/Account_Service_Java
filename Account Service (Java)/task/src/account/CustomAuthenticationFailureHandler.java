package account;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("TESTPOINT: customAuthenticationFailureHandler");
        if (exception instanceof LockedException) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setTimestamp(LocalDateTime.now());
            errorDTO.setStatus(HttpStatus.FORBIDDEN.value());
            errorDTO.setMessage("User account is locked.");
            errorDTO.setError(exception.getMessage());
            errorDTO.setPath(request.getContextPath());

            ObjectMapper mapper = new ObjectMapper();

            response.getOutputStream().println(mapper.writeValueAsString(errorDTO));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println("TESTPOINT: customAuthenticationFailureHandler (else branch)");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
