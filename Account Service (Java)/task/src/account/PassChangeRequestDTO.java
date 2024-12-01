package account;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
@JsonPropertyOrder({"new_password"})
public class PassChangeRequestDTO {

    @NotBlank
    @JsonProperty("new_password")
    private String newPass;

    public PassChangeRequestDTO() {}

    public PassChangeRequestDTO(String newPass) {
        this.newPass = newPass;
    }

    @JsonProperty("new_password")
    public String getNewPass() {
        return newPass;
    }

    @JsonProperty("new_password")
    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}
