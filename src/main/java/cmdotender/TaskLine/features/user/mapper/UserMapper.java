package cmdotender.TaskLine.features.user.mapper;

import cmdotender.TaskLine.features.user.dto.UserDTO;
import cmdotender.TaskLine.features.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

    void updateEntityFromDto(UserDTO dto, @MappingTarget User entity);
}
