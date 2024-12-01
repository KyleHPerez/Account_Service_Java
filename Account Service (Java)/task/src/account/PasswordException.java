package account;

public class PasswordException extends BadRequestException {

    public PasswordException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
