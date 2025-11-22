package ua.kpi.ia33.shellweb.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.kpi.ia33.shellweb.domain.User;
import ua.kpi.ia33.shellweb.patterns.state.op.*;
import ua.kpi.ia33.shellweb.repo.UserRepository;
import ua.kpi.ia33.shellweb.service.FileOpsService;
import ua.kpi.ia33.shellweb.service.SearchService;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

// === Prototype only ===
import ua.kpi.ia33.shellweb.patterns.prototype.fs.OperationPrototypes;
import ua.kpi.ia33.shellweb.patterns.prototype.fs.FsOperation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.kpi.ia33.shellweb.patterns.factorymethod.DefaultFsOperationFactory;
import ua.kpi.ia33.shellweb.patterns.factorymethod.dto.OperationRequest;
import ua.kpi.ia33.shellweb.patterns.factorymethod.dto.OperationType;

@Controller

@RequestMapping("/ops")
public class OperationController {

    private final SearchService searchService;
    private final FileOpsService fileOpsService;
    private final UserRepository users;

    // Реєстр прототипів
    private final OperationPrototypes prototypes;

    public OperationController(SearchService searchService,
                               FileOpsService fileOpsService,
                               UserRepository users,
                               OperationPrototypes prototypes) {
        this.searchService = searchService;
        this.fileOpsService = fileOpsService;
        this.users = users;
        this.prototypes = prototypes;
    }

    @SuppressWarnings("unchecked")
    private Map<String, OperationContext> ops(HttpSession session) {
        Object m = session.getAttribute(SessionKeys.OPS_MAP);
        if (m == null) {
            Map<String, OperationContext> created = new LinkedHashMap<>();
            session.setAttribute(SessionKeys.OPS_MAP, created);
            return created;
        }
        return (Map<String, OperationContext>) m;
    }

    private OperationContext newCtx() {
        return new OperationContext(searchService, fileOpsService);
    }

    // --- запуск
    @PostMapping("/start")
    public String start(
            @RequestParam("type") OperationType type,
            @RequestParam(required = false) String nameMask,
            @RequestParam(required = false) String ext,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String target,
            HttpSession session
    ) {
        // --- будуємо запит БЕЗ фабричних методів
        OperationRequest req = new OperationRequest(type);

        switch (type) {
            case SEARCH -> {
                Long userId = (Long) session.getAttribute(SessionKeys.USER_ID);
                if (userId == null) return "redirect:/login";
                User user = users.findById(userId).orElseThrow();
                req.user(user)
                        .nameMask(nameMask)
                        .ext(ext);
            }
            case COPY -> {
                if (source == null || target == null)
                    throw new IllegalArgumentException("source/target must be provided for COPY");
                req.source(Path.of(source))
                        .target(Path.of(target));
            }
            case MOVE -> {
                if (source == null || target == null)
                    throw new IllegalArgumentException("source/target must be provided for MOVE");
                req.source(Path.of(source))
                        .target(Path.of(target));
            }
            case DELETE -> {
                if (source == null)
                    throw new IllegalArgumentException("source must be provided for DELETE");
                req.source(Path.of(source));
            }
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        }

        OperationContext ctx = newCtx();

        // Клонуємо ОБРАНИЙ ПРОТОТИП і передаємо у контекст
        FsOperation op = prototypes.prototypeOf(type).clone().withRequest(req);
        ctx.setOperation(op);

        ops(session).put(ctx.id(), ctx);

        // синхронний запуск (для ЛР достатньо)
        ctx.start(req);

        return "redirect:/ops/" + ctx.id();
    }

    // --- статус
    @GetMapping("/{id}")
    public String status(@PathVariable String id, HttpSession session, Model model) {
        OperationContext ctx = ops(session).get(id);
        if (ctx == null) {
            model.addAttribute("notFound", true);
            return "ops";
        }
        model.addAttribute("id", ctx.id());
        model.addAttribute("state", ctx.getState().name());
        model.addAttribute("progress", ctx.getProgress());
        model.addAttribute("message", ctx.getMessage());
        model.addAttribute("error", ctx.getError());
        model.addAttribute("terminal", ctx.getState().isTerminal());
        return "ops";
    }

    // --- скасування
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable String id, HttpSession session) {
        OperationContext ctx = ops(session).get(id);
        if (ctx != null) ctx.cancel();
        return "redirect:/ops/" + id;
    }

    // --- список останніх
    @GetMapping
    public String list(HttpSession session, Model model) {
        model.addAttribute("items", ops(session).values());
        return "ops";
    }
}
