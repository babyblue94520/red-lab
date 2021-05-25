package pers.clare.post.auth;

import pers.clare.common.util.WebUtil;

import javax.servlet.http.HttpSession;

public class AuthAware {
    public static final String USER = "user";

    private AuthAware() {
    }

    public static void setUserId(Long id) {
        HttpSession session = WebUtil.session(true);
        session.setAttribute(USER, id);
    }

    public static Long getUserId() {
        HttpSession session = WebUtil.session(false);
        if (session == null) return null;
        return (Long) session.getAttribute(USER);
    }


    public static Long getUserIdAndCheck() {
        Long id = getUserId();
        if (id == null) throw new IllegalArgumentException("Not permission");
        return id;
    }
}
