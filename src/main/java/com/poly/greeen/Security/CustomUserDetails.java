package com.poly.greeen.Security;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
public class CustomUserDetails implements UserDetails {
    private final AuthUser authUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.authUser::getRole);
    }

    @Override
    public String getPassword() {
        return this.authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.authUser.getUniqueId();
    }
}
