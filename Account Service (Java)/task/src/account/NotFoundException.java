package account;

public class NotFoundException extends BadRequestException {

    public NotFoundException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
