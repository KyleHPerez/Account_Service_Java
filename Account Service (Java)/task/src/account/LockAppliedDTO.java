package account;

public class LockAppliedDTO {
    private String status;

    public LockAppliedDTO() {}

    public LockAppliedDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
