package account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class AppUserAdapter implements UserDetails {

    private final AppUser appUser;

    public AppUserAdapter(AppUser appUser) {
        this.appUser = appUser;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return appUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getName();
    }

    public String getEmail() {
        return appUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return appUser.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return appUser.isAccountEnabled();
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
