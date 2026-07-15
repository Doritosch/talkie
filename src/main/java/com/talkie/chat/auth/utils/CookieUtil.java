package com.talkie.chat.auth.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";

    public static void createCookie(HttpServletResponse http, String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "Strict");
        cookie.setPath("/");
        http.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletResponse http) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, null);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "Strict");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        http.addCookie(cookie);
    }
}
