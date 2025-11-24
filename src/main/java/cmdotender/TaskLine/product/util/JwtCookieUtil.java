package cmdotender.TaskLine.product.util;

import cmdotender.TaskLine.product.security.config.JwtProperties;
import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

@UtilityClass
public final class JwtCookieUtil {

    public static ResponseCookie buildAccessCookie(String token, JwtProperties props) {
        var c = props.getCookie();
        return base(c.getName(), token, props.getAccessTtl(), props)
                .build();
    }

    public static ResponseCookie clearAccessCookie(JwtProperties props) {
        var c = props.getCookie();
        return base(c.getName(), "", Duration.ZERO, props)
                .maxAge(0)
                .build();
    }


    private static ResponseCookie.ResponseCookieBuilder base(
            String name, String value, Duration maxAge, JwtProperties props
    ) {
        var c = props.getCookie();
        var b = ResponseCookie.from(name,value)
                .httpOnly(c.isHttpOnly())
                .secure(c.isSecure())
                .path(c.getPath())
                .maxAge(maxAge)
                .sameSite(c.getSameSite().name());
        if(c.getDomain() != null && !c.getDomain().isBlank()) {
            b.domain(c.getDomain());
        }
        return b;
    }
}
