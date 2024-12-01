package account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRepository extends CrudRepository<RoleGroup, Long> {
    RoleGroup findByName(String name);

    @Query("SELECT g FROM RoleGroup g JOIN g.userRoles r WHERE r IN :userRoles")
    RoleGroup findByUserRoles(List<UserRole> userRoles);
}

