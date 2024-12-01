package account;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerAuth {

    private final ServiceAuth authService;

    @Autowired
    public ControllerAuth(ServiceAuth authService) {
        this.authService = authService;
    }

    @PostMapping("api/auth/signup")
    public ResponseEntity<AppUserDTO> signup(@Valid @RequestBody RegistrationRequestDTO request, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(authService.createUser(request, httpRequest));
    }

    @PostMapping("api/auth/changepass")
    public ResponseEntity<PasswordUpdateSuccessDTO> changepass(@AuthenticationPrincipal AppUserAdapter adapter,
                                                               @Valid @RequestBody PassChangeRequestDTO passChangeRequestDTO) {
        return ResponseEntity.ok(authService.changePassword(adapter, passChangeRequestDTO));
    }
}
