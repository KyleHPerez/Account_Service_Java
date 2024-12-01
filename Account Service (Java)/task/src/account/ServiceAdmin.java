package account;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.event.spi.EventSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ServiceAdmin {
    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final SecurityEventRepository securityEventRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public ServiceAdmin(AppUserRepository userRepository, RoleRepository roleRepository, GroupRepository groupRepository, SecurityEventRepository securityEventRepository, ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.securityEventRepository = securityEventRepository;
        this.publisher = publisher;
    }

    public AppUserDTO modifyRoles(RoleChangeRequestDTO request) {
        System.out.println(request.getRole());
        AppUser user = userRepository.findByEmail(request.getAppUser()).orElseThrow(() -> new NotFoundException("User not found!"));
        AppUserAdapter adapter = (AppUserAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser admin = adapter.getAppUser();
        SecurityEvent userModifiedEvent = new SecurityEvent();
        userModifiedEvent.setDate(LocalDateTime.now());
        userModifiedEvent.setUser(admin);
        userModifiedEvent.setSubject(admin.getEmail().toLowerCase());
        userModifiedEvent.setPath("/api/admin/user/role");

        // Granting a Role:
        if (request.getOperation().equals("GRANT")) {
            userModifiedEvent.setAction("GRANT_ROLE");
            RoleGroup userRoleGroup = groupRepository.findByUserRoles(user.getRoles());
            RoleGroup requestRoleGroup = groupRepository.findByUserRoles(List.of(roleRepository.findByName("ROLE_" + request.getRole()).orElseThrow(() -> new NotFoundException("Role not found!"))));
            if (userRoleGroup.equals(requestRoleGroup)) {
                user.getRoles().add(roleRepository.findByName("ROLE_" + request.getRole()).orElseThrow(() -> new NotFoundException("Role not found!")));
                userModifiedEvent.setObject("Grant role " + request.getRole() + " to " + user.getEmail().toLowerCase());
                userRepository.save(user);
            } else {
                throw new AdministrativeException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
            }
            // Removing a role:
        } else if (request.getOperation().equals("REMOVE")) {
            userModifiedEvent.setAction("REMOVE_ROLE");
            // If trying to remove administrator role:
            if (request.getRole().equals("ADMINISTRATOR")) {
                throw new AdministrativeException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
            }
            // If the user only has one role:
            if (user.getRoles().size() == 1 && user.getRoles().contains(roleRepository.findByName("ROLE_" + request.getRole()).orElseThrow(() -> new NotFoundException("Role not found!")))) {
                throw new AdministrativeException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
            }
            // If the user does not have the role:
            if (!user.getRoles().contains(roleRepository.findByName("ROLE_" + request.getRole()).orElseThrow(() -> new NotFoundException("Role not found!")))) {
                throw new AdministrativeException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
            }
            user.getRoles().remove(roleRepository.findByName("ROLE_" + request.getRole()).orElseThrow(() -> new NotFoundException("Role not found!")));
            userModifiedEvent.setObject("Remove role " + request.getRole() + " from " + user.getEmail().toLowerCase());
            userRepository.save(user);
        }
        // Publish security event:
        publisher.publishEvent(userModifiedEvent);
        return user.createDTO();
    }

    public List<AppUserDTO> getUsers() {
        List<AppUser> users = (List<AppUser>) userRepository.findAll();
        users.sort(Comparator.comparingLong(AppUser::getId));
        return users.stream().map(AppUser::createDTO).toList();
    }

    public AppUserDeleteResponseDTO deleteUser(AppUserAdapter adapter, String email) {
        AppUser user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
        AppUser admin = adapter.getAppUser();
        // If trying to remove administrator:
        if (user.getRoles().contains(roleRepository.findByName("ROLE_ADMINISTRATOR").orElseThrow(() -> new NotFoundException("Role not found!")))) {
            throw new AdministrativeException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
        // Delete and publish deletion event:
        List<SecurityEvent> deletedUserEvents = securityEventRepository.findByUser(user);
        for (SecurityEvent deletedUserEvent : deletedUserEvents) {
            deletedUserEvent.setUser(null);
            securityEventRepository.save(deletedUserEvent);
        }
        userRepository.delete(user);
        SecurityEvent userDeletedEvent = new SecurityEvent();
        userDeletedEvent.setDate(LocalDateTime.now());
        userDeletedEvent.setAction("DELETE_USER");
        userDeletedEvent.setUser(admin);
        userDeletedEvent.setSubject(admin.getEmail().toLowerCase());
        userDeletedEvent.setObject(user.getEmail().toLowerCase());
        userDeletedEvent.setPath("/api/admin/user");
        publisher.publishEvent(userDeletedEvent);
        return new AppUserDeleteResponseDTO(email,"Deleted successfully!");
    }

    public LockAppliedDTO modAccess(AccessChangeRequestDTO request, HttpServletRequest httpRequest, AppUserAdapter adminAdapter) {
        LockAppliedDTO lockAppliedDTO = new LockAppliedDTO();
        AppUser user = userRepository.findByEmail(request.getUser()).orElseThrow(() -> new NotFoundException("User not found!"));
        AppUser adminUser = adminAdapter.getAppUser();
        if (request.getOperation().equals("LOCK")) {
            if (user.getRoles().contains(roleRepository.findByName("ROLE_ADMINISTRATOR").orElseThrow(() -> new NotFoundException("Role not found!")))) {
                throw new BadRequestException("Can't lock the ADMINISTRATOR!");
            }
            user.setAccountNonLocked(false);
            userRepository.save(user);
            SecurityEvent userLockedEvent = new SecurityEvent();
            userLockedEvent.setDate(LocalDateTime.now());
            userLockedEvent.setAction("LOCK_USER");
            userLockedEvent.setUser(adminUser);
            userLockedEvent.setSubject(adminUser.getEmail().toLowerCase());
            userLockedEvent.setObject("Lock user " + user.getEmail().toLowerCase());
            userLockedEvent.setPath("/api/admin/user/role");
            publisher.publishEvent(userLockedEvent);
            lockAppliedDTO.setStatus("User " + user.getEmail() + " locked!");
        }
        if (request.getOperation().equals("UNLOCK")) {
            user.setAccountNonLocked(true);
            user.setFailedAttempts(0);
            userRepository.save(user);
            SecurityEvent userUnlockedEvent = new SecurityEvent();
            userUnlockedEvent.setDate(LocalDateTime.now());
            userUnlockedEvent.setAction("UNLOCK_USER");
            userUnlockedEvent.setUser(adminUser);
            userUnlockedEvent.setSubject(adminUser.getEmail().toLowerCase());
            userUnlockedEvent.setObject("Unlock user " + user.getEmail().toLowerCase());
            userUnlockedEvent.setPath("/api/admin/user/role");
            publisher.publishEvent(userUnlockedEvent);
            lockAppliedDTO.setStatus("User " + user.getEmail() + " unlocked!");
        }
        return lockAppliedDTO;
    }
}
