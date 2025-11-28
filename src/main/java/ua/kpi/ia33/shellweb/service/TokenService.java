package ua.kpi.ia33.shellweb.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.kpi.ia33.shellweb.domain.User;
import ua.kpi.ia33.shellweb.repo.UserRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    private final UserRepository users;

    private final Map<String, Long> tokens = new ConcurrentHashMap<>();

    public TokenService(UserRepository users) {
        this.users = users;
    }


    public String issueTokenFor(User user) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, user.getId());
        return token;
    }


    public User requireUser(String token) {
        if (token == null || token.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Missing authentication token"
            );
        }

        Long userId = tokens.get(token);
        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid or expired token"
            );
        }

        return users.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found"
                ));
    }
}
