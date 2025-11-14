package cmdotender.TaskLine.features.auth.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface JwtService {
    String generateAccessToken(String username, List<String> roles, Map<String,Object> extraClaims);
    String generateAccessToken(String username, List<String> roles);
    boolean isValid(String token);
    String username(String token);
    List<String> roles(String token);
}
