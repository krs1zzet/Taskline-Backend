package cmdotender.TaskLine.features.role.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleRequest {

    private String code;
    private String name;
    private String description;

}
