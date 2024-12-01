package account;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<UserRole, Long> {
    Optional<UserRole> findByName(String name);
}
