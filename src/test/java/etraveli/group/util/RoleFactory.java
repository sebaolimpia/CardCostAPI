package etraveli.group.util;

import static etraveli.group.model.RoleEnum.USER;

import etraveli.group.model.Role;

public class RoleFactory {
    public static Role getRoleWithoutIdRole() {
        return Role.builder()
                .name(USER)
                .description("User role")
                .build();
    }
}
