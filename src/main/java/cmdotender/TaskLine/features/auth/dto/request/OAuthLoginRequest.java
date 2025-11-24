package cmdotender.TaskLine.features.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OAuthLoginRequest {

    @NotBlank
    private String externalId;

    @Email
    @NotBlank
    private String email;
}
