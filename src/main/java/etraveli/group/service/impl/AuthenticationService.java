package etraveli.group.service.impl;

import static etraveli.group.common.Constants.*;
import static etraveli.group.model.RoleEnum.USER;

import etraveli.group.dto.RoleDto;
import etraveli.group.dto.request.UserRequestDto;
import etraveli.group.dto.response.UserLoginResponseDto;
import etraveli.group.dto.response.UserSignUpResponseDto;
import etraveli.group.exception.BadRequestException;
import etraveli.group.exception.NotFoundException;
import etraveli.group.model.Role;
import etraveli.group.model.Users;
import etraveli.group.model.UsersRole;
import etraveli.group.repository.IRoleRepository;
import etraveli.group.repository.IUsersRepository;
import etraveli.group.service.IAuthenticationService;
import etraveli.group.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationService implements IAuthenticationService {

    private final JwtService jwtService;

    private final IRoleRepository roleRepository;

    private final IUsersRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(JwtService jwtService, IRoleRepository roleRepository,
                                 IUsersRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user, where the password is stored encrypted.
     * By default, the "USER" role is assigned to the user.
     * It is expected that the user does not exist previously, if it does not exist an exception is thrown.
     *
     * @param userRequestDto User data to register.
     * @return UserSignUpResponseDto with the user data.
     * */
    @Override
    public UserSignUpResponseDto signUp(UserRequestDto userRequestDto){
        Users user = createUser(userRequestDto);
        user = userRepository.save(user);
        return UserSignUpResponseDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .userRoles(user.getUsersRoles().stream()
                        .map(userRol -> RoleDto.builder()
                                .name(userRol.getRole().getName())
                                .description(userRol.getRole().getDescription())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private Users createUser(UserRequestDto userRequestDto) {
        // By default, users are created with the minimum privilege.
        Optional<Role> optionalRol = roleRepository.findByName(USER);
        if (optionalRol.isEmpty()) {
            throw new NotFoundException(EXCEPTION_USER_ROL_NOT_FOUND);
        }

        // Avoid messages that include information of database DataIntegrityViolationException.
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            throw new BadRequestException(String.format(
                    EXCEPTION_ALREADY_EXIST_USER_WITH_USERNAME,
                    userRequestDto.getUsername()));
        }

        Users user = new Users(userRequestDto.getUsername(),
                passwordEncoder.encode(userRequestDto.getPassword()));
        List<UsersRole> userRoles = List.of(
                UsersRole.builder()
                        .role(optionalRol.get())
                        .user(user)
                        .build());
        user.setUsersRoles(userRoles);
        return user;
    }

    /**
     * Perform user authentication.
     * If the user and password are correct, a JWT token is generated.
     * The hash of the entered password is compared with the stored hash (the password is not decrypted).
     *
     * @param userRequestDto User authentication data.
     * @return UserLoginResponseDto with the user data.
     */
    @Override
    public UserLoginResponseDto login(UserRequestDto userRequestDto) {
        Users user = userRepository.findByUsername(userRequestDto.getUsername()).orElse(null);
        if (user != null && passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            return UserLoginResponseDto.builder()
                    .id(user.getIdUser())
                    .username(user.getUsername())
                    .token(jwtService.createJWT(user))
                    .roles(user.getUsersRoles().stream()
                            .map(userRol -> RoleDto.builder()
                                    .name(userRol.getRole().getName())
                                    .description(userRol.getRole().getDescription())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();
        } else {
            throw new NotFoundException(EXCEPTION_INVALID_USERNAME_OR_PASSWORD);
        }
    }
}
