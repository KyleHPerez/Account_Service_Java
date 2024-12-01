package account;

public class GeneralSuccessDTO {
    private String status;

    public GeneralSuccessDTO() {}

    public GeneralSuccessDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
