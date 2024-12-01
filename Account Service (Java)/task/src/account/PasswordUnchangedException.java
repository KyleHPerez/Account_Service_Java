package account;

public class PasswordUnchangedException extends PasswordException {

    public PasswordUnchangedException() {
        super("The passwords must be different!");
    }

    public String getMessage() {
        return super.getMessage();
    }
}
