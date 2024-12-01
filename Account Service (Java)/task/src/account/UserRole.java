package account;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserRole {
    @Id
    @GeneratedValue
    private long id;

    private String name;

    @ManyToOne
    private RoleGroup roleGroup;

    @ManyToMany
    private List<AppUser> users;

    public UserRole() {}

    public UserRole(String name) {
        this.name = name;
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

    public void setName(String role) {
        this.name = role;
    }
}
