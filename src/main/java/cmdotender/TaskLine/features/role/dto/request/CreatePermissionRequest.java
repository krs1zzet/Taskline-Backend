package cmdotender.TaskLine.features.role.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionRequest {
    private String code;
    private String description;

}
