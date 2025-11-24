package cmdotender.TaskLine.features.auth.dto;

import cmdotender.TaskLine.features.user.dto.UserDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDTO {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
}
