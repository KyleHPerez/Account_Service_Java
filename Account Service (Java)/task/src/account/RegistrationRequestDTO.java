package account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
@JsonPropertyOrder({"name", "lastname", "email", "password"})
public class RegistrationRequestDTO {

    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotBlank
    @JsonProperty("lastname")
    private String lastname;

    @NotBlank
    @Pattern(regexp="[^@]+@acme.com")
    @JsonProperty("email")
    private String email;

    @NotBlank
    @JsonProperty("password")
    private String password;


    public RegistrationRequestDTO() {}

    public RegistrationRequestDTO(String name, String email, String password) {
        this.name = name;
        this.lastname = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
