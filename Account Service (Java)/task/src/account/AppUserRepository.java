package account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<AppUser> findByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM AppUser u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) != 0 THEN TRUE ELSE FALSE END FROM AppUser u")
    boolean exists();
}
