package ua.servicedesk.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ua.servicedesk.dao.RoleRepository;
import ua.servicedesk.domain.AllowedLink;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class CustomHttpSecurityConfig {

    private RoleRepository roleRepository;

    private void addAccess(HttpSecurity http){

        List<AllowedLink> roles = roleRepository.findAllLinks();

        roles.forEach(role->{
            String[] rolesNames = new String[role.getRoles().size()];
            for (int i = 0; i < role.getRoles().size(); i++) {
                rolesNames[i] = role.getRoles().get(i).getName();
            }
            try {
                http
                        .authorizeRequests()
                        .requestMatchers(role.getLink()).hasAnyAuthority(rolesNames);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                .cors().disable()
                .csrf().disable()
                .formLogin()
//                .loginPage("/login")
////                .failureUrl("/login")
                .defaultSuccessUrl("/requests")
                .and()

                .logout()
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true);

                addAccess(http);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

}
