package account;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleUserExistException(BadRequestException e, WebRequest request) {
        return getErrorDTOResponseEntity(HttpStatus.BAD_REQUEST, request, e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolationException(ConstraintViolationException e,
                                                                       WebRequest request) {
        return getErrorDTOResponseEntity(HttpStatus.BAD_REQUEST, request, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                          WebRequest request) {
        return getErrorDTOResponseEntity(HttpStatus.BAD_REQUEST, request, e);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleUserNotFoundException(NotFoundException e, WebRequest request) {
        return getErrorDTOResponseEntity(HttpStatus.NOT_FOUND, request, e);
    }

    @ExceptionHandler(AdministrativeException.class)
    public ResponseEntity<ErrorDTO> handleAdministrativeException(AdministrativeException e, WebRequest request) {
        System.out.println("Handled by administrative exception handler");
        return getErrorDTOResponseEntity(e.getStatus(), request, e);
    }

    private ResponseEntity<ErrorDTO> getErrorDTOResponseEntity(HttpStatus status, WebRequest request, Exception e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setStatus(status.value());
        errorDTO.setError(status.getReasonPhrase());
        errorDTO.setMessage(e.getMessage());
        errorDTO.setTimestamp(LocalDateTime.now());
        errorDTO.setPath(request.getDescription(false).split("=")[1]);
        return new ResponseEntity<>(errorDTO, status);
    }
}
