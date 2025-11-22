package ua.kpi.ia33.shellweb.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.kpi.ia33.shellweb.domain.SearchQuery;
import ua.kpi.ia33.shellweb.domain.User;
import ua.kpi.ia33.shellweb.repo.UserRepository;
import ua.kpi.ia33.shellweb.service.SearchService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SearchController {
    private final SearchService search;
    private final UserRepository users;
    private final SessionKeys session;

    public SearchController(SearchService search, UserRepository users, SessionKeys session) {
        this.search = search;
        this.users = users;
        this.session = session;
    }

    private User currentUser(HttpServletRequest req) {
        Long id = session.getUserId(req);
        return users.findById(id).orElseThrow();
    }

    @GetMapping("/search")
    public String page(HttpServletRequest req, Model model) {
        User u = currentUser(req);
        model.addAttribute("queries", search.listQueries(u));
        return "search";
    }

    @PostMapping("/search")
    public String doSearch(
            HttpServletRequest req,
            @RequestParam(name = "nameMask", required = false) String nameMask,
            @RequestParam(name = "ext", required = false) String ext,
            @RequestParam(name = "mode", defaultValue = "bfs") String mode) {
        User u = currentUser(req);
        SearchQuery q = search.createQuery(u, nameMask, ext, mode);
        return "redirect:/results/" + q.getId();
    }


    @GetMapping("/results/{id}")
    public String results(@PathVariable("id") Long id, HttpServletRequest req, Model model) {
        User u = currentUser(req);

        // Отримуємо запит з бази напряму
        SearchQuery q = search.findQueryByIdForUser(id, u);
        if (q == null) {
            model.addAttribute("error", "Запит не знайдено або недоступний");
            return "search";
        }

        model.addAttribute("query", q);
        model.addAttribute("results", search.resultsFor(q));
        return "results";
    }

}
