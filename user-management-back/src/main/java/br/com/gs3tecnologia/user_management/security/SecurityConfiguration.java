package br.com.gs3tecnologia.user_management.security;

import br.com.gs3tecnologia.user_management.exception.UserNotFoundException;
import br.com.gs3tecnologia.user_management.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) ->
                        authorize
//                                .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()
//                                .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                                .requestMatchers(antMatcher("/public/**")).permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(httpBasicConfigurer_ -> {
                    httpBasicConfigurer_.realmName("user-management");
                    httpBasicConfigurer_.authenticationEntryPoint(new BasicAuthenticationEntryPoint() {
                        @Override
                        public void afterPropertiesSet() {
                            setRealmName("user-management");
                            super.afterPropertiesSet();
                        }
                        @Override
                        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException {
                            var mapper = new ObjectMapper();
                            mapper.registerModule(new JavaTimeModule());
                            HttpStatus status = HttpStatus.UNAUTHORIZED;

                            ProblemDetail problemDetail = ProblemDetail
                                    .forStatusAndDetail(
                                            HttpStatus.UNAUTHORIZED,
                                            """
                                                    Access denied: The provided credentials are invalid.
                                                    Please check your username and password and try again.
                                                    """);
                            response.setStatus(status.value());
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(mapper.writeValueAsString(problemDetail));
                        }
                    });
                })
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            var user = userRepository.findByUsername(username).orElseThrow(
                    () -> new UserNotFoundException(
                            """
                               User with username %s not found 
                            """.formatted(username)
                    ));
            return new CustomUser(
                    user.getId(),
                    user.getUsername(),
                    user.getSecret(),
                    user.getProfile() != null ?
                            List.of(new SimpleGrantedAuthority(user
                                    .getProfile().getName().toUpperCase())) :
                            Collections.emptyList()
            );
        };
    }
}
