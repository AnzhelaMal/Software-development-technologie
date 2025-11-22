package ua.kpi.ia33.shellweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.ia33.shellweb.domain.SearchQuery;
import ua.kpi.ia33.shellweb.domain.User;

import java.util.List;

public interface SearchQueryRepository extends JpaRepository<SearchQuery, Long> {
    List<SearchQuery> findByUserOrderByCreatedAtDesc(User user);
}
