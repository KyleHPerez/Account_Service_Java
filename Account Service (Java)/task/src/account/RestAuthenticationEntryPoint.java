package account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        response.setContentType("application/json");
        if (authException instanceof LockedException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ErrorDTO errorResponse = new ErrorDTO();
            errorResponse.setTimestamp(LocalDateTime.now());
            errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            errorResponse.setMessage("User account is locked");
            errorResponse.setError("Unauthorized");
            errorResponse.setPath(request.getRequestURI());
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());

                String jsonResponse = mapper.writeValueAsString(errorResponse);
                response.getWriter().write(jsonResponse);
            } catch (Exception e) {
                response.getWriter().write("{\"error\": \"Internal error\", \"message\": \"Failed to process error response\"}");
            }
        } else if (authException instanceof AccountStatusException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Account status issue\", \"message\": \"Account status issue: " + authException.getMessage() + "\"}");
        } else {
            System.out.println("TESTPOINT: RestAuthenticationEntryPoint");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("\"error\": \"Unauthorized access\", \"message\": \"Unauthorized access\"}");
        }
    }
}