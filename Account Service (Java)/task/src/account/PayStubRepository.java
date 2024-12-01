package account;

import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PayStubRepository extends CrudRepository<PayStub, Long> {

    List<PayStub> findByEmployeeId(long employeeId);

    boolean existsByEmployeeAndPeriod(AppUser employee, @Pattern(regexp = "(0[1-9]|1[0-2])-[0-9]{4}") String period);

    Optional<PayStub> findByEmployeeAndPeriod(AppUser employee, @Pattern(regexp = "(0[1-9]|1[0-2])-[0-9]{4}") String period);
}
