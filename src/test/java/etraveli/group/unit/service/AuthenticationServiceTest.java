package etraveli.group.unit.service;

import etraveli.group.dto.request.UserRequestDto;
import etraveli.group.dto.response.UserLoginResponseDto;
import etraveli.group.dto.response.UserSignUpResponseDto;
import etraveli.group.exception.NotFoundException;
import etraveli.group.model.Users;
import etraveli.group.repository.IRoleRepository;
import etraveli.group.repository.IUsersRepository;
import etraveli.group.service.JwtService;
import etraveli.group.service.impl.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static etraveli.group.util.RoleFactory.getRoleWithoutIdRole;
import static etraveli.group.util.UserDtoFactory.*;
import static etraveli.group.util.UserFactory.getUserWithoutIdUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private IUsersRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Test sign up successful.")
    public void signUpSuccessfulTest() {
        // GIVEN
        UserSignUpResponseDto expectedUserSignUpResponseDto = getUserSignUpResponseDto();

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername(expectedUserSignUpResponseDto.getUsername());
        userRequestDto.setPassword(expectedUserSignUpResponseDto.getPassword());

        Users usersToSave = mapUserSignUpResponseDtoToUser(expectedUserSignUpResponseDto);

        when(passwordEncoder.encode(anyString())).thenReturn(expectedUserSignUpResponseDto.getPassword());
        when(roleRepository.findByName(any())).thenReturn(Optional.of(getRoleWithoutIdRole()));
        when(userRepository.save(any(Users.class))).thenReturn(usersToSave);

        // WHEN
        UserSignUpResponseDto currentUserSignUpResponseDto = authenticationService.signUp(userRequestDto);

        // THEN
        InOrder inOrder = inOrder(passwordEncoder, userRepository);
        inOrder.verify(passwordEncoder).encode(anyString());
        inOrder.verify(userRepository).save(usersToSave);
        assertEquals(expectedUserSignUpResponseDto, currentUserSignUpResponseDto);
    }

    @Test
    @DisplayName("Test sign up with username already exist.")
    public void signUpWithUsernameAlreadyExistTest() {
        // GIVEN
        UserSignUpResponseDto userSignUpResponseDto = getUserSignUpResponseDto();

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername(userSignUpResponseDto.getUsername());
        userRequestDto.setPassword(userSignUpResponseDto.getPassword());

        when(passwordEncoder.encode(anyString())).thenReturn(userSignUpResponseDto.getPassword());
        when(roleRepository.findByName(any())).thenReturn(Optional.of(getRoleWithoutIdRole()));
        when(userRepository.save(any(Users.class))).thenThrow(DataIntegrityViolationException.class);

        // WHEN

        // THEN
        assertThrows(DataIntegrityViolationException.class, () ->
                authenticationService.signUp(userRequestDto));
        InOrder inOrder = inOrder(passwordEncoder, userRepository);
        inOrder.verify(passwordEncoder).encode(anyString());
        inOrder.verify(userRepository).save(any(Users.class));
    }

    @Test
    @DisplayName("Test login successful.")
    public void loginSuccessfulTest() {
        // GIVEN
        UserLoginResponseDto expectedUserLoginResponseDto = getUserLoginResponseDto();

        UserRequestDto userRequestDto = getUserRequestDto();

        Users usersToLogin = mapUserLoginResponseDtoToUser(expectedUserLoginResponseDto);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(usersToLogin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.createJWT(any(Users.class))).thenReturn(expectedUserLoginResponseDto.getToken());

        // WHEN
        UserLoginResponseDto currentUserLoginResponseDto =
                authenticationService.login(userRequestDto);

        // THEN
        InOrder inOrder = inOrder(userRepository, passwordEncoder, jwtService);
        inOrder.verify(userRepository).findByUsername(anyString());
        inOrder.verify(passwordEncoder).matches(anyString(), anyString());
        inOrder.verify(jwtService).createJWT(any(Users.class));
        assertEquals(expectedUserLoginResponseDto, currentUserLoginResponseDto);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Test login users does not exist.")
    void loginEmptyUserTest(Users nullUsers) {
        // GIVEN
        UserRequestDto userLoginRequestDto = getUserRequestDto();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(nullUsers));

        // WHEN

        // THEN
        assertThrows(NotFoundException.class, () -> authenticationService.login(userLoginRequestDto));
        verify(userRepository).findByUsername(anyString());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("Test login users with incorrect password.")
    void loginUnMatchPasswordTest() {
        // GIVEN
        UserRequestDto userLoginRequestDto = getUserRequestDto();

        Users usersToLogin = getUserWithoutIdUser();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(usersToLogin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // WHEN

        // THEN
        assertThrows(NotFoundException.class, () -> authenticationService.login(userLoginRequestDto));
        verify(userRepository).findByUsername(anyString());
        verify(passwordEncoder).matches(anyString(), anyString());
        verifyNoInteractions(jwtService);
    }
}
