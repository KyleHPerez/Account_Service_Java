package account;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
@JsonPropertyOrder({"user", "role", "operation"})
public class RoleChangeRequestDTO {
    @NotBlank
    private String user;
    @NotBlank
    private String role;
    @NotBlank
    @Pattern(regexp="(GRANT|REMOVE)")
    private String operation;

    public RoleChangeRequestDTO() {}

    public RoleChangeRequestDTO(String user, String role, String operation) {
        this.user = user;
        this.role = role;
        this.operation = operation;
    }

    public String getAppUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
