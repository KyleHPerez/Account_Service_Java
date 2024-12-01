package account;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SecurityEventRepository extends CrudRepository<SecurityEvent,Long> {

    void deleteByUser(AppUser user);

    List<SecurityEvent> findByUser(AppUser user);
}
