package ua.servisedesk.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ua.servisedesk.dao.RoleRepository;
import ua.servisedesk.domain.Role;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class CustomHttpSecurityConfig {
    @Autowired
    RoleRepository roleRepository;
    private String[] roles(){
        List<Role> roles = roleRepository.findAllItems();
        String[] rolesNames = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            rolesNames[i] = roles.get(i).getName();
        }
        return rolesNames;
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
                .clearAuthentication(true)

                .and()
                .authorizeRequests()
                .requestMatchers("/**").hasAnyAuthority(roles())

        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
