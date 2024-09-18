package etraveli.group.util;

import static etraveli.group.model.RoleEnum.USER;

import etraveli.group.dto.RoleDto;

public class RoleDtoFactory {
    public static RoleDto getRoleDto() {
        return RoleDto.builder()
                .name(USER)
                .description("User role")
                .build();
    }
}
