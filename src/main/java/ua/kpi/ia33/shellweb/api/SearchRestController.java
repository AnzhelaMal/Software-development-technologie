package ua.kpi.ia33.shellweb.api;

import org.springframework.web.bind.annotation.*;
import ua.kpi.ia33.shellweb.domain.SearchQuery;
import ua.kpi.ia33.shellweb.domain.SearchResult;
import ua.kpi.ia33.shellweb.domain.User;
import ua.kpi.ia33.shellweb.service.SearchService;
import ua.kpi.ia33.shellweb.service.TokenService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class SearchRestController {

    private final SearchService searchService;
    private final TokenService tokenService;

    public SearchRestController(SearchService searchService,
                                TokenService tokenService) {
        this.searchService = searchService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public SearchIdResponse startSearch(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody SearchRequest request
    ) {
        User user = tokenService.requireUser(token);

        SearchQuery query = searchService.createQuery(
                user,
                request.getNameMask(),
                request.getExt()
        );

        return new SearchIdResponse(query.getId());
    }

    @GetMapping("/{id}/status")
    public SearchStatusResponse status(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable("id") Long id
    ) {
        User user = tokenService.requireUser(token);
        SearchQuery q = searchService.findQueryByIdForUser(id, user);
        if (q == null) {
            throw new IllegalArgumentException("Search query not found");
        }

        List<SearchResult> results = searchService.resultsFor(q);

        return new SearchStatusResponse(
                q.getId(),
                q.getNameMask(),
                q.getExt(),
                q.getCreatedAt(),
                results.size(),
                "DONE"
        );
    }

    @GetMapping("/{id}/results")
    public List<SearchResultDto> results(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable("id") Long id
    ) {
        User user = tokenService.requireUser(token);
        SearchQuery q = searchService.findQueryByIdForUser(id, user);
        if (q == null) {
            throw new IllegalArgumentException("Search query not found");
        }

        List<SearchResult> list = searchService.resultsFor(q);

        return list.stream()
                .map(r -> new SearchResultDto(
                        r.getPath(),
                        r.getName(),
                        r.getType()
                ))
                .collect(Collectors.toList());
    }
}
