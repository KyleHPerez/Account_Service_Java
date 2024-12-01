package account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SecurityEventLogger {

    private final SecurityEventRepository securityEventRepository;

    @Autowired
    public SecurityEventLogger(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }

    @EventListener
    public void onSecurityEvent(SecurityEvent event) {
        securityEventRepository.save(event);
    }
}
