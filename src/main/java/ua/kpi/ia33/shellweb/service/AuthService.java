package ua.kpi.ia33.shellweb.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.ia33.shellweb.domain.User;
import ua.kpi.ia33.shellweb.repo.UserRepository;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository users;

    public AuthService(UserRepository users) {
        this.users = users;
    }

    @Transactional
    public User register(String email, String password) {
        users.findByEmail(email).ifPresent(u -> { throw new IllegalArgumentException("Email already used"); });
        User u = new User();
        u.setEmail(email.trim().toLowerCase());
        u.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
        return users.save(u);
    }

    public Optional<User> authenticate(String email, String password) {
        return users.findByEmail(email.trim().toLowerCase())
                .filter(u -> BCrypt.checkpw(password, u.getPasswordHash()));
    }
}
