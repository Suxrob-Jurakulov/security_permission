package com.company.config;

import com.company.dto.ProfileModuleDto;
import com.company.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private Set<GrantedAuthority> authorities;

    public CustomUserDetails(UserEntity entity) {
        username = entity.getUsername();
        password = entity.getPassword();
        authorities = (Set<GrantedAuthority>) mapModulesToAuthorities(entity.getModules());

    }

    // MAPPERS
    public Collection<? extends GrantedAuthority> mapModulesToAuthorities(List<ProfileModuleDto> modules) {
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
        if (modules != null && !modules.isEmpty()) {
            for (ProfileModuleDto module : modules) {
                if (module != null) {
                    for (String permission : module.getPermissions()) {
                        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(String.format("%s:%s", module.getId(), permission)));
                    }
                }
            }
        }
        return simpleGrantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}