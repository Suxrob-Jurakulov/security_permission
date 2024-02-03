package com.company.config;

import com.company.domains.Profile;
import com.company.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String profileId) throws UsernameNotFoundException {
        Optional<Profile> user = profileRepository.findByIdAndDeletedIsFalse(profileId);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.map((CustomUserDetails::new))
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + profileId));

    }
}