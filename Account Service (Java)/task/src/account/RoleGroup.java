package account;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class RoleGroup {
    @Id
    @GeneratedValue
    private long id;

    private String name;

    @OneToMany
    private List<UserRole> userRoles;

    public RoleGroup() {}

    public RoleGroup(String name, List<UserRole> userRoles) {
        this.name = name;
        this.userRoles = userRoles;
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

    public List<UserRole> getRoles() {
        return userRoles;
    }

    public void setUsers(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}
