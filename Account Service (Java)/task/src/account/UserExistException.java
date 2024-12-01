package account;

public class UserExistException extends BadRequestException {
    public UserExistException() {
        super("User exist!");
    }
}
