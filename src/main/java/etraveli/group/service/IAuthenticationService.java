package etraveli.group.service;

import etraveli.group.dto.request.UserRequestDto;
import etraveli.group.dto.response.UserLoginResponseDto;
import etraveli.group.dto.response.UserSignUpResponseDto;
import etraveli.group.exception.NotFoundException;

public interface IAuthenticationService {

    UserSignUpResponseDto signUp(UserRequestDto userSignUpRequestDto);

    UserLoginResponseDto login(UserRequestDto userLoginRequestDto) throws NotFoundException;
}
