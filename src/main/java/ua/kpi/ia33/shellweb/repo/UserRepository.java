package ua.kpi.ia33.shellweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.ia33.shellweb.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
