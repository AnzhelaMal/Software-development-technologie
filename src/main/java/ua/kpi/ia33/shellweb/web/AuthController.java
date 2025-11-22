package ua.kpi.ia33.shellweb.web;

import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kpi.ia33.shellweb.domain.User;
import ua.kpi.ia33.shellweb.repo.UserRepository;
import ua.kpi.ia33.shellweb.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@Validated
public class AuthController {

    private final AuthService auth;
    private final SessionKeys session;
    private final UserRepository users;

    public AuthController(AuthService auth, SessionKeys session, UserRepository users) {
        this.auth = auth;
        this.session = session;
        this.users = users;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            HttpServletRequest req,
            @RequestParam("email") @Email String email,
            @RequestParam("password") String password,
            Model model) {

        return auth.authenticate(email, password)
                .map(u -> {
                    session.setUserId(req, u.getId());
                    return "redirect:/search";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Невірна пошта або пароль");
                    return "login";
                });
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            HttpServletRequest req,
            @RequestParam("email") @Email String email,
            @RequestParam("password") String password,
            Model model) {

        try {
            User u = auth.register(email, password);
            session.setUserId(req, u.getId());
            return "redirect:/search";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest req) {
        session.logout(req);
        return "redirect:/login";
    }
}
