package etraveli.group.controller;

import static etraveli.group.common.Constants.*;

import etraveli.group.dto.request.UserRequestDto;
import etraveli.group.dto.response.UserLoginResponseDto;
import etraveli.group.dto.response.UserSignUpResponseDto;
import etraveli.group.exception.NotFoundException;
import etraveli.group.service.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(LOCALHOST_URL)
@RequestMapping(AUTHORIZATION_BASE_URL)
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponseDto> signUp(@RequestBody @Valid UserRequestDto registerUserDto) {
        UserSignUpResponseDto registeredUser = authenticationService.signUp(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserRequestDto userLoginRequestDto)
            throws NotFoundException {
        UserLoginResponseDto userLogin = authenticationService.login(userLoginRequestDto);
        return ResponseEntity.ok(userLogin);
    }
}
