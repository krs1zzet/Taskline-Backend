package cmdotender.TaskLine.features.auth.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignUpRequest {
    private String username;
    private String password;
    private Boolean enabled;
}
