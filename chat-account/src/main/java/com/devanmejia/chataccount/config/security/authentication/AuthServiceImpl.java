package com.devanmejia.chataccount.config.security.authentication;

import com.devanmejia.chataccount.model.user.State;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public boolean hasPermission(State state) {
        Authentication authentication = getAuthentication();
        if (authentication.isAuthenticated()){
            UserDetails userDetails = (UserDetails) getAuthentication().getPrincipal();
            if (userDetails.isAccountNonExpired() && userDetails.isAccountNonLocked()
                    && userDetails.isCredentialsNonExpired() && userDetails.isEnabled()){
                return userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(authority -> authority.equals(state.name()));
            }
        }
        return false;
    }

    @Override
    public String getUserName() {
        return ((UserDetails) getAuthentication().getPrincipal()).getUsername();
    }

    private Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
