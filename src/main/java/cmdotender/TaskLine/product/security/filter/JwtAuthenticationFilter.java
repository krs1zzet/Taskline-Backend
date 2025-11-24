package cmdotender.TaskLine.product.security.filter;

import cmdotender.TaskLine.features.auth.service.JwtService;
import cmdotender.TaskLine.product.security.config.JwtProperties;
import cmdotender.TaskLine.product.util.AuthHeaderUtil;
import cmdotender.TaskLine.product.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtProperties props;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {

        if(SecurityContextHolder.getContext().getAuthentication() !=null){
            chain.doFilter(request,response);
            return;
        }



        String token = CookieUtil.getValue(request, props.getCookie().getName());
        if(token == null){
            token = AuthHeaderUtil.getBearerToken(request);
            log.info("JWT Filter - Bearer token: {}", token);
        }
        if(token == null  || !jwtService.isValid(token)){
            chain.doFilter(request,response);
            return;
        }
        String username = jwtService.username(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);

        var tokenAuthorities = jwtService.roles(token).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        var authorities = user.getAuthorities().isEmpty() ? tokenAuthorities : user.getAuthorities();

        var auth = new UsernamePasswordAuthenticationToken(user,null, authorities);
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(auth);
        log.info("JWT Filter - Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
        chain.doFilter(request,response);
    }
}
