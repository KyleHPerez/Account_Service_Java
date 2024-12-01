package account;

public class AccessChangeRequestDTO {
    private String user;
    private String operation;

    public AccessChangeRequestDTO() {}

    public AccessChangeRequestDTO(String user, String operation) {
        this.user = user;
        this.operation = operation;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
