package cmdotender.TaskLine.features.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AtlassianUserInfoDTO {
    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("email") // gerçek responseda email farklı gelebilir, gerekirse burayı adjust edersin
    private String email;

    @JsonProperty("name")
    private String name;
}
