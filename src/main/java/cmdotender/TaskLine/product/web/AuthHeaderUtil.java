package cmdotender.TaskLine.product.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class AuthHeaderUtil {

    public static String getBearerToken(HttpServletRequest request) {
        String h = request.getHeader("Authorization");
        if(h == null) {return null;}
        if(!h.regionMatches(true,0,"Bearer ",0,7)) return null;
        return h.substring(7).trim();
    }
}
