package ua.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.requestfields.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    private CurrentRoleHolder roleHolder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User dbUser = userRepository.findUserByName(username);
                if (dbUser == null) {
                    return null;
                }

                roleHolder.setRole(dbUser.getRole());
                roleHolder.setUserId(dbUser.getId());
                roleHolder.setUserName(dbUser.getName());

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {

                List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
                grantedAuthorityList.add(new SimpleGrantedAuthority(dbUser.getRole().getName()));
                return grantedAuthorityList;
            }

            @Override
            public String getPassword() {
                return dbUser.getPassword();
            }

            @Override
            public String getUsername() {
                return dbUser.getName();
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
                return dbUser.getIsEnabled();
            }
        };
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setRoleHolder(CurrentRoleHolder roleHolder) {
        this.roleHolder = roleHolder;
    }

}
