package account;

public class PasswordLengthException extends PasswordException {

    public PasswordLengthException() {
        super("Password length must be 12 chars minimum!");
    }

    public String getMessage() {
        return super.getMessage();
    }
}
