package account;

import org.springframework.http.HttpStatus;

public class AdministrativeException extends RuntimeException {
    private final HttpStatus status;

    public AdministrativeException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
