package cmdotender.TaskLine.features.auth.dto;
import lombok.Data;

import java.util.List;
@Data
public class AccessibleResourceDTO {

        private String id;
        private String url;
        private String name;
        private List<String> scopes;
        private String avatarUrl;
        private String access;


}
