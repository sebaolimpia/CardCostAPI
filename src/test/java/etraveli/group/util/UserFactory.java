package etraveli.group.util;

import static etraveli.group.util.RoleFactory.getRoleWithoutIdRole;

import etraveli.group.model.Users;
import etraveli.group.model.UsersRole;
import net.bytebuddy.utility.RandomString;
import java.util.List;

public class UserFactory {

    public static Users getUserWithoutIdUser() {
        Users user = new Users();
        user.setUsername(RandomString.make());
        user.setPassword(RandomString.make());
        user.setUsersRoles(List.of(
                UsersRole.builder()
                        .role(getRoleWithoutIdRole())
                        .user(user)
                        .build()));
        return user;
    }
}
