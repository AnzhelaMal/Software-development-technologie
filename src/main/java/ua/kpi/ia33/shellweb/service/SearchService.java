package ua.kpi.ia33.shellweb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.ia33.shellweb.domain.SearchQuery;
import ua.kpi.ia33.shellweb.domain.SearchResult;
import ua.kpi.ia33.shellweb.domain.User;
import ua.kpi.ia33.shellweb.repo.SearchQueryRepository;
import ua.kpi.ia33.shellweb.repo.SearchResultRepository;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final SearchQueryRepository queries;
    private final SearchResultRepository results;

    public SearchService(SearchQueryRepository queries, SearchResultRepository results) {
        this.queries = queries;
        this.results = results;
    }

    // 🔹 основний метод (3 параметри)
    @Transactional
    public SearchQuery createQuery(User user, String nameMask, String ext) {
        return createQueryInternal(user, nameMask, ext);
    }

    // 🔹 перевантаження для сумісності з контролером (4 параметри)
    @Transactional
    public SearchQuery createQuery(User user, String nameMask, String ext, String mode) {
        // mode наразі не використовується
        return createQuery(user, nameMask, ext);
    }

    /** 🔹 Шукаємо лише на диску D:\, ігноруємо недоступні теки/файли. Ліміт — 1000 записів. */
    protected SearchQuery createQueryInternal(User user, String nameMask, String ext) {
        SearchQuery q = new SearchQuery();
        q.setUser(user);
        q.setNameMask(nameMask);
        q.setExt(ext);
        q.setCreatedAt(Instant.now());
        q = queries.save(q);

        final SearchQuery savedQuery = q;

        Path root = Paths.get("D:\\"); // шукаємо лише на D:\
        results.deleteByQuery(savedQuery);

        List<SearchResult> toSave = new ArrayList<>();

        try {
            Files.walkFileTree(root, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        if (attrs.isRegularFile() && matches(file, nameMask, ext)) {
                            SearchResult r = new SearchResult();
                            r.setQuery(savedQuery);
                            r.setPath(file.toString());
                            r.setName(file.getFileName().toString());
                            r.setType("file");
                            toSave.add(r);

                            if (toSave.size() >= 1000) {
                                return FileVisitResult.TERMINATE;
                            }
                        }
                    } catch (Exception ignore) {
                        // пропускаємо проблемні файли
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    // не падаємо на недоступних теках/файлах
                    return FileVisitResult.SKIP_SUBTREE;
                }
            });
        } catch (IOException e) {
            log.warn("Search walk error: {}", e.getMessage());
        }

        results.saveAll(toSave);
        return savedQuery;
    }

    private boolean matches(Path path, String nameMask, String ext) {
        String fileName = path.getFileName().toString().toLowerCase();
        String core = (nameMask == null) ? "" : nameMask.replace("*", "").toLowerCase();

        boolean nameOk = core.isEmpty() || fileName.contains(core);
        boolean extOk = (ext == null || ext.isBlank()) || fileName.endsWith("." + ext.toLowerCase());
        return nameOk && extOk;
    }

    public List<SearchQuery> listQueries(User user) {
        return queries.findByUserOrderByCreatedAtDesc(user);
    }

    public SearchQuery findQueryByIdForUser(Long id, User user) {
        return queries.findById(id)
                .filter(q -> q.getUser().getId().equals(user.getId()))
                .orElse(null);
    }

    public List<SearchResult> resultsFor(SearchQuery q) {
        return results.findByQuery(q);
    }




}
