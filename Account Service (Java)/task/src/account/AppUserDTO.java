package account;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@JsonPropertyOrder({"id", "name", "lastname", "email", "roles"})
public class AppUserDTO {
    private long id;
    private String name;
    private String lastname;
    private String email;
    private List<String> userRoles;

    public AppUserDTO() {}

    public AppUserDTO(long id, String name, String lastname, String email, List<UserRole> userRoles) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email.toLowerCase();
        List<String> roles = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            roles.add(userRole.getName());
        }
        roles.sort(Comparator.naturalOrder());
        this.userRoles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        this.email = email.toLowerCase();
    }

    public List<String> getRoles() {
        return userRoles;
    }

    public void setRoles(List<UserRole> userRoles) {
        List<String> roles = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            roles.add(userRole.getName());
        }
        roles.sort(Comparator.naturalOrder());
        this.userRoles = roles;
    }
}
