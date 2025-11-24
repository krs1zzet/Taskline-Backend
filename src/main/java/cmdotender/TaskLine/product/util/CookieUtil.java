package cmdotender.TaskLine.product.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public final class CookieUtil {

    public static Optional<Cookie> get(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return Optional.empty();
        }
        for (Cookie c : cookies) {
            if(name.equals(c.getName())) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
    public static String getValue(HttpServletRequest request, String name){

        Optional<Cookie> cookie = get(request, name);
        if(cookie != null && cookie.isPresent()){
            return cookie.get().getValue();
        }else{
            return null;
        }
    }

    public static boolean has(HttpServletRequest request, String name){
        return get(request, name).isPresent();
    }

    public static Map<String,Cookie> toMap(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Map.of();
        }

        Map<String, Cookie> map = new HashMap<>();
        for (Cookie cookie : cookies) {
            map.put(cookie.getName(), cookie);
        }
        return map;
    }

}
