package account;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class ControllerBusiness {

    private final ServiceBusiness serviceBusiness;
    private final AppUserRepository repository;

    @Autowired
    public ControllerBusiness(ServiceBusiness serviceBusiness, AppUserRepository repository) {
        this.repository = repository;
        this.serviceBusiness = serviceBusiness;
    }

    @GetMapping("api/empl/payment")
    public ResponseEntity<Object> getPayrolls(@AuthenticationPrincipal AppUserAdapter adapter,
                                                  @RequestParam(required = false) String period) {
        AppUser user = adapter.getAppUser();
        if (null == period) {
            return ResponseEntity.ok(serviceBusiness.getPayRecord(user));
        } else {
            return ResponseEntity.ok(serviceBusiness.getPayRecord(user, period));
        }
    }

    @PostMapping("api/acct/payments")
    public ResponseEntity<GeneralSuccessDTO> uploadPayrolls(@RequestBody List<@Valid PayStubEntryDTO> entries) {
        serviceBusiness.addPayStubs(entries);
        return ResponseEntity.ok().body(new GeneralSuccessDTO("Added successfully!"));
    }

    @PutMapping("api/acct/payments")
    public ResponseEntity<GeneralSuccessDTO> updatePaymentInfo(@RequestBody @Valid PayStubEntryDTO update) {
        serviceBusiness.updatePayStub(update);
        return ResponseEntity.ok().body(new GeneralSuccessDTO("Updated successfully!"));
    }

    @GetMapping("/api/security/events/")
    public ResponseEntity<List<SecurityEventDTO>> getSecurityEvents() {
        return ResponseEntity.ok(serviceBusiness.getSecurityEvents());
    }
}
