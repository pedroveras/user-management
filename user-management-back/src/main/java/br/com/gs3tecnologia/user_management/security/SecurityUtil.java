package br.com.gs3tecnologia.user_management.security;

import br.com.gs3tecnologia.user_management.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    private final UserRepository userRepository;

    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authUserIdentification(String username) {
        var user = getPrincipal();
        return user != null && user.getUsername().equals(username);
    }

    public boolean authUser(String id) {
        var user = getPrincipal();
        return user != null && user.getId().equals(id);
    }

    public CustomUser getPrincipal() {
        try {
            return (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException ignored) {
            return null;
        }
    }

    public String getUserName() {
        try {
            return getPrincipal().getId();
        } catch (final NullPointerException ignored) {
            return "anonymous";
        }
    }
}
