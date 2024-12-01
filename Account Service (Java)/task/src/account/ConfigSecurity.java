package account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class ConfigSecurity {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, AppUserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        return authenticationManagerBuilder.build();
    }

    @Bean RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        CustomAuthenticationProvider provider = new CustomAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RestAuthenticationEntryPoint restAuthenticationEntryPoint) throws Exception {
        return http
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(restAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler))
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(auth -> auth
                        //ANYONE
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/shutdown").permitAll()
                        //BUSINESS GROUP ONLY
                        .requestMatchers(HttpMethod.POST, "/api/auth/changepass").authenticated()
                        .requestMatchers("/api/empl/payment").hasAnyRole("USER", "ACCOUNTANT")
                        .requestMatchers("/api/acct/payments").hasRole("ACCOUNTANT")
                        .requestMatchers("/api/security/events/").hasRole("AUDITOR")
                        //ADMINISTRATIVE GROUP ONLY
                        .requestMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
                )
                .sessionManagement(sessions -> sessions
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
