package ua.kpi.ia33.shellweb.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class WebInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Дозволені шляхи без авторизації
        if (uri.startsWith("/login") || uri.startsWith("/register") ||
                uri.startsWith("/css/") || uri.startsWith("/h2") || uri.startsWith("/error")) {
            return true;
        }

        HttpSession s = request.getSession(false);
        boolean loggedIn = s != null && s.getAttribute(SessionKeys.USER_ID) != null;

        if (!loggedIn) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
