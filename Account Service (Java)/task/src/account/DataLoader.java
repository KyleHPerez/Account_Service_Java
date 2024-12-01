package account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("singleton")
public class DataLoader {
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public DataLoader(GroupRepository groupRepository, RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        createRoles();
    }

    private void createRoles() {
        if (roleRepository.count() == 0) {
            try {
                UserRole administrator = new UserRole("ROLE_ADMINISTRATOR");
                UserRole accountant = new UserRole("ROLE_ACCOUNTANT");
                UserRole user = new UserRole("ROLE_USER");
                UserRole auditor = new UserRole("ROLE_AUDITOR");
                roleRepository.save(administrator);
                roleRepository.save(accountant);
                roleRepository.save(user);
                roleRepository.save(auditor);

                List<UserRole> administrativeUserRoles = List.of(administrator);
                RoleGroup administrative = new RoleGroup("ADMINISTRATIVE", administrativeUserRoles);
                List<UserRole> businessUserRoles = List.of(accountant, user, auditor);
                RoleGroup business = new RoleGroup("BUSINESS", businessUserRoles);
                groupRepository.save(administrative);
                groupRepository.save(business);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
