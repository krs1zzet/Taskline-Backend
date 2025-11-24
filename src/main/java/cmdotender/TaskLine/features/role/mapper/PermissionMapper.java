package cmdotender.TaskLine.features.role.mapper;

import cmdotender.TaskLine.features.role.dto.PermissionDTO;
import cmdotender.TaskLine.features.role.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toEntity(PermissionDTO permissionDTO);

    PermissionDTO toDTO(Permission permission);

    void updateEntityFromDTO(PermissionDTO permissionDTO, @MappingTarget Permission permission);

}
