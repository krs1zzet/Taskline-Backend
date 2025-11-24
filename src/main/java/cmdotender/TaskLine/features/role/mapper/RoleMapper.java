package cmdotender.TaskLine.features.role.mapper;

import cmdotender.TaskLine.features.role.dto.RoleDTO;
import cmdotender.TaskLine.features.role.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO toDTO(Role role);

    Role toEntity(RoleDTO roleDTO);

    void updateEntityFromDto(RoleDTO dto, @MappingTarget Role entity);

}
