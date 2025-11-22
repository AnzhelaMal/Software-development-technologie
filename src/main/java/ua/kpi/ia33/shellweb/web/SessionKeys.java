package ua.kpi.ia33.shellweb.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionKeys {
    public static final String USER_ID = "userId";

    public Long getUserId(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s == null ? null : (Long) s.getAttribute(USER_ID);
    }

    public void setUserId(HttpServletRequest req, Long id) {
        req.getSession(true).setAttribute(USER_ID, id);
    }

    public void logout(HttpServletRequest req) {
        var s = req.getSession(false);
        if (s != null) s.invalidate();
    }

    public static final String OPS_MAP = "opsMap";
}
