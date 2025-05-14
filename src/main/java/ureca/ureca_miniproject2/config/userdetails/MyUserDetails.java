package ureca.ureca_miniproject2.config.userdetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
@AllArgsConstructor
public class MyUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Getter
    private final Integer userId;
    private final String username;

    @Getter
    private String name;
    private final String password;

    @Getter
    private final String profileImageUrl;

    private final Collection<? extends GrantedAuthority> authorities;

    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override public boolean isAccountNonExpired() { return true; } //나중에 디테일한 설정을 위한 메소드, 일단은 모두 true
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

}
