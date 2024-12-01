package account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Entity
@Validated
@JsonPropertyOrder({"id", "name", "lastname", "email"})
public class AppUser {

    public AppUser() {}

    public AppUser(long id, String name, String lastname, String email, String password, List<PayStub> payStubs, List<UserRole> userRoles, boolean accountEnabled, boolean accountNonLocked) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.payStubs = payStubs;
        this.userRoles = userRoles;
        this.accountEnabled = accountEnabled;
        this.accountNonLocked = accountNonLocked;
    }

    @Id
    @GeneratedValue
    @Column
    @JsonView(Views.Public.class)
    @JsonProperty("id")
    private long id;

    @JsonView(Views.Public.class)
    @JsonProperty("name")
    @NotBlank
    private String name;

    @JsonView(Views.Public.class)
    @JsonProperty("lastname")
    @NotBlank
    private String lastname;

    @JsonView(Views.Public.class)
    @JsonProperty("email")
    @NotBlank
    @Pattern(regexp="[^@]+@acme.com")
    private String email;

    @JsonView(Views.Internal.class)
    @NotBlank
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private List<PayStub> payStubs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SecurityEvent> securityEvents;

    private int failedAttempts = 0;

    private boolean accountNonLocked = true;

    private boolean accountEnabled = true;

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public List<SecurityEvent> getSecurityEvents() {
        return securityEvents;
    }

    public void setSecurityEvents(List<SecurityEvent> securityEvents) {
        this.securityEvents = securityEvents;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
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
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserRole> getRoles() {
        return userRoles;
    }

    public void setRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public List<PayStub> getPayStubs() {
        return payStubs;
    }

    public void setPayStubs(List<PayStub> payStubs) {
        this.payStubs = payStubs;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean status) {
        this.accountNonLocked = status;
    }

    public boolean isAccountEnabled() {
        return accountEnabled;
    }

    public void setAccountEnabled(boolean status) {
        this.accountEnabled = status;
    }

    public void incrementFailedAttempts() {
        failedAttempts++;
    }

    public AppUserDTO createDTO() {
        AppUserDTO dto = new AppUserDTO();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setLastname(this.lastname);
        dto.setEmail(this.email);
        dto.setRoles(this.userRoles);
        return dto;
    }
}
