package ua.kpi.ia33.shellweb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.ia33.shellweb.domain.SearchQuery;
import ua.kpi.ia33.shellweb.domain.SearchResult;

import java.util.List;

public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {
    List<SearchResult> findByQuery(SearchQuery query);
    void deleteByQuery(SearchQuery query);
}
