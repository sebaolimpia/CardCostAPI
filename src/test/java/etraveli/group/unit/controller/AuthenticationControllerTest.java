package etraveli.group.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import etraveli.group.controller.AuthenticationController;
import etraveli.group.dto.request.UserRequestDto;
import etraveli.group.dto.response.UserLoginResponseDto;
import etraveli.group.dto.response.UserSignUpResponseDto;
import etraveli.group.service.IAuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private IAuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    @DisplayName("Sign up user successful.")
    public void signUpTest(){
        // GIVEN
        UserSignUpResponseDto expected = new UserSignUpResponseDto();

        UserRequestDto userRequestDto = new UserRequestDto();
        when(authenticationService.signUp(any(UserRequestDto.class))).thenReturn(expected);

        // WHEN
        ResponseEntity<UserSignUpResponseDto> current = authenticationController.signUp(userRequestDto);

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(HttpStatus.OK, current.getStatusCode());
    }

    @Test
    @DisplayName("Login user successfull.")
    public void loginSuccessfulTest(){
        // GIVEN
        UserLoginResponseDto expected = new UserLoginResponseDto();

        UserRequestDto userRequestDto = new UserRequestDto();
        when(authenticationService.login(any(UserRequestDto.class))).thenReturn(expected);

        // WHEN
        ResponseEntity<UserLoginResponseDto> current = authenticationController.login(userRequestDto);

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(HttpStatus.OK, current.getStatusCode());
    }
}
