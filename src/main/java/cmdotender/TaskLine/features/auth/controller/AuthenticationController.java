package cmdotender.TaskLine.features.auth.controller;

import cmdotender.TaskLine.features.auth.dto.AtlassianTokenDTO;
import cmdotender.TaskLine.features.auth.dto.AuthDTO;
import cmdotender.TaskLine.features.auth.dto.request.ChangePasswordRequest;
import cmdotender.TaskLine.features.auth.dto.request.OAuthLoginRequest;
import cmdotender.TaskLine.features.auth.dto.request.SignInRequest;
import cmdotender.TaskLine.features.auth.dto.request.SignUpRequest;
import cmdotender.TaskLine.features.auth.service.AtlassianOauthService;
import cmdotender.TaskLine.features.auth.service.AuthenticationService;
import cmdotender.TaskLine.features.user.dto.UserDTO;
import cmdotender.TaskLine.features.user.service.UserService;
import cmdotender.TaskLine.product.security.config.AtlassianOAuthProperties;
import cmdotender.TaskLine.product.security.config.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtProperties jwtProperties;



    @PostMapping("/sign-up")
    public ResponseEntity<UserDTO> signUp(@RequestBody @Valid SignUpRequest request) {
        UserDTO userDTO = authenticationService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserDTO> signIn(@RequestBody @Valid SignInRequest request) {

        AuthDTO authDTO = authenticationService.signIn(request);


        String cookieName = jwtProperties.getCookie().getName();
        boolean cookieSecure = jwtProperties.getCookie().isSecure();
        long ttlSeconds = jwtProperties.getAccessTtlSeconds();

        ResponseCookie cookie = ResponseCookie.from(cookieName, authDTO.getAccessToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(ttlSeconds)
                .domain(jwtProperties.getCookie().getDomain())
                .sameSite("Lax")
                .build();

        UserDTO userDTO = userService.findByUsername(request.getUsername());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userDTO);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid ChangePasswordRequest request,
            @AuthenticationPrincipal UserDetails principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDTO currentUser = userService.findByUsername(principal.getUsername());
        authenticationService.changePassword(currentUser.getId(), request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut() {
        String cookieName = jwtProperties.getCookie().getName();
        boolean cookieSecure = jwtProperties.getCookie().isSecure();

        ResponseCookie cleared = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleared.toString())
                .build();
    }

    @PostMapping("/oauth/{provider}")
    public ResponseEntity<UserDTO> loginWithOAuth(
            @PathVariable String provider,
            @RequestBody @Valid OAuthLoginRequest request
    ) {
        AuthDTO auth = authenticationService.loginWithOAuth(
                provider,
                request.getExternalId(),
                request.getEmail(),
                null
        );

        String cookieName = jwtProperties.getCookie().getName();
        boolean cookieSecure = jwtProperties.getCookie().isSecure();
        long ttlSeconds = jwtProperties.getAccessTtlSeconds();

        ResponseCookie cookie = ResponseCookie.from(cookieName, auth.getAccessToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(ttlSeconds)
                .sameSite("Lax")
                .build();

        UserDTO userDTO = userService.findByUsername(request.getEmail());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(
            @AuthenticationPrincipal UserDetails principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDTO currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }



}
