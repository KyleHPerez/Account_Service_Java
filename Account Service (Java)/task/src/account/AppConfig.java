package account;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@Import({ConfigSecurity.class})
public class AppConfig {
}
