package account;

public class PasswordBreachedException extends PasswordException {
    private String path;

    public PasswordBreachedException() {
        super("The password is in the hacker's database!");
    }

    public String getMessage() {
        return super.getMessage();
    }
}
