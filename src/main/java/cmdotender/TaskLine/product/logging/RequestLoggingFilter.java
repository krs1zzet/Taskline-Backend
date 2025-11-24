package cmdotender.TaskLine.product.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        long start = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("reqId", requestId);

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String fullPath = (query != null && !query.isBlank()) ? uri + "?" + query : uri;
        String remoteIp = request.getRemoteAddr();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            int status = response.getStatus();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String principal = (auth != null) ? auth.getName() : "anonymous";

            log.info("""
                    [REQ {}]
                      Method   : {}
                      Path     : {}
                      Status   : {}
                      Duration : {} ms
                      Remote   : {}
                      User     : {}
                    """,
                    requestId,
                    method,
                    fullPath,
                    status,
                    duration,
                    remoteIp,
                    principal
            );

            MDC.clear();
        }
    }
}
