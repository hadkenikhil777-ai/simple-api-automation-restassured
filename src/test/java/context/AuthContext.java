package context;

import java.util.Map;

public class AuthContext {

    private static String accessToken;
    private static String refreshToken;
    private static Map<String, String> cookies;

    private AuthContext() {
        // prevent object creation
    }

    // -------- Access Token --------
    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String token) {
        accessToken = token;
    }

    // -------- Refresh Token --------
    public static String getRefreshToken() {
        return refreshToken;
    }

    public static void setRefreshToken(String token) {
        refreshToken = token;
    }

    // -------- Cookies --------
    public static Map<String, String> getCookies() {
        return cookies;
    }

    public static void setCookies(Map<String, String> cookieMap) {
        cookies = cookieMap;
    }

    // -------- Reset (important) --------
    public static void clear() {
        accessToken = null;
        refreshToken = null;
        cookies = null;
    }
}
