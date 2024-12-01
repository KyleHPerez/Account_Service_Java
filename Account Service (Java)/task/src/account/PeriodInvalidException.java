package account;

public class PeriodInvalidException extends BadRequestException {

    public PeriodInvalidException() {
        super("Period invalid. Format must be mm-YYYY.");
    }
}
