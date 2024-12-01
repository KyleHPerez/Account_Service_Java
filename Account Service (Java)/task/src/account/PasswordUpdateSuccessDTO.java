package account;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"email", "status"})
public class PasswordUpdateSuccessDTO {
    private String email;
    private String status;

    public PasswordUpdateSuccessDTO() {}

    public PasswordUpdateSuccessDTO(String email) {
        this.email = email.toLowerCase();
        this.status = "The password has been updated successfully";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
