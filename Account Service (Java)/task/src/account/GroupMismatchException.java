package account;

public class GroupMismatchException extends RuntimeException {
    public GroupMismatchException() {
        super("The appUser cannot combine administrative and business roles!");
    }
}
