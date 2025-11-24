package cmdotender.TaskLine.features.auth.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SignInRequest {
    private String username;
    private String password;
}
