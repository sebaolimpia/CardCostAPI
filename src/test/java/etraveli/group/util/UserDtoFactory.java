package etraveli.group.util;

import etraveli.group.dto.request.UserRequestDto;
import etraveli.group.dto.response.UserLoginResponseDto;
import etraveli.group.dto.response.UserSignUpResponseDto;
import etraveli.group.model.Users;
import etraveli.group.model.UsersRole;
import net.bytebuddy.utility.RandomString;

import java.util.List;

import static etraveli.group.util.RoleDtoFactory.getRoleDto;
import static etraveli.group.util.RoleFactory.getRoleWithoutIdRole;

public class UserDtoFactory {
    public static UserSignUpResponseDto getUserSignUpResponseDto() {
        return UserSignUpResponseDto.builder()
                .username(RandomString.make())
                .password(RandomString.make())
                .userRoles(List.of(getRoleDto()))
                .build();
    }

    public static UserLoginResponseDto getUserLoginResponseDto() {
        return UserLoginResponseDto.builder()
                .username(RandomString.make())
                .token(RandomString.make())
                .roles(List.of(getRoleDto()))
                .build();
    }

    public static UserRequestDto getUserRequestDto() {
        return UserRequestDto.builder()
                .username(RandomString.make())
                .password(RandomString.make())
                .build();
    }

    public static Users mapUserSignUpResponseDtoToUser(UserSignUpResponseDto userSignUpResponseDto) {
        Users user = new Users();
        user.setUsername(userSignUpResponseDto.getUsername());
        user.setPassword(userSignUpResponseDto.getPassword());
        user.setUsersRoles(List.of(
                UsersRole.builder()
                        .role(getRoleWithoutIdRole())
                        .user(user)
                        .build()));
        return user;
    }

    public static Users mapUserLoginResponseDtoToUser(UserLoginResponseDto userLoginResponseDto) {
        return Users.builder()
                .username(userLoginResponseDto.getUsername())
                .password(RandomString.make())
                .usersRoles(List.of(
                        UsersRole.builder()
                                .role(getRoleWithoutIdRole())
                                .build()))
                .build();
    }
}
