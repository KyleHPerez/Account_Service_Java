package account;

public class PayPeriodException extends BadRequestException {

    public PayPeriodException() {
        super("Multiple pay stubs cannot be allocated in one pay period to the same employee!");
    }

    public String getMessage() {
        return super.getMessage();
    }
}
