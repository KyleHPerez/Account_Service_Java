package account;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.locks.Lock;

@RestController
public class ControllerAdmin {

    private final ServiceAdmin adminService;

    @Autowired
    public ControllerAdmin(ServiceAdmin adminService) {
        this.adminService = adminService;
    }

    @PutMapping("api/admin/user/role")
    public ResponseEntity<AppUserDTO> setRole(@RequestBody @Valid RoleChangeRequestDTO request) {
        return ResponseEntity.ok(adminService.modifyRoles(request));
    }

    @DeleteMapping("api/admin/user/{email}")
    public ResponseEntity<AppUserDeleteResponseDTO> deleteUser(@AuthenticationPrincipal AppUserAdapter adminAdapter, @PathVariable String email) {
        return ResponseEntity.ok(adminService.deleteUser(adminAdapter, email));
    }

    @GetMapping("api/admin/user/")
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getUsers());
    }

    @PutMapping("api/admin/user/access")
    public ResponseEntity<LockAppliedDTO> modUserAccess(@AuthenticationPrincipal AppUserAdapter adminAdapter, @RequestBody @Valid AccessChangeRequestDTO request, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(adminService.modAccess(request, httpRequest, adminAdapter));
    }
}
