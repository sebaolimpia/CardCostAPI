package etraveli.group.service;

import etraveli.group.dto.UsersDto;
import etraveli.group.dto.request.UserRequestDto;
import etraveli.group.dto.response.UserListDto;

public interface IUsersService {

    UserListDto getAllUsers();

    UsersDto createAdministrator(UserRequestDto userRequestDto);
}
