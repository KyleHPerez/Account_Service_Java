package account;

public class AppUserDeleteResponseDTO {
    private String user;
    private String status;

    public AppUserDeleteResponseDTO() {}

    public AppUserDeleteResponseDTO(String user, String status) {
        this.user = user;
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setAppUser(String appUser) {
        this.user = appUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
