package etraveli.group.service.impl;

import static etraveli.group.common.Constants.EXCEPTION_ADMINISTRATOR_ROLE_NOT_FOUND;
import static etraveli.group.common.Constants.EXCEPTION_ALREADY_EXIST_USER_WITH_USERNAME;
import static etraveli.group.model.RoleEnum.ADMIN;

import etraveli.group.dto.RoleDto;
import etraveli.group.dto.UsersDto;
import etraveli.group.dto.request.UserRequestDto;
import etraveli.group.dto.response.UserListDto;
import etraveli.group.exception.BadRequestException;
import etraveli.group.exception.NotFoundException;
import etraveli.group.model.Role;
import etraveli.group.model.Users;
import etraveli.group.model.UsersRole;
import etraveli.group.repository.IRoleRepository;
import etraveli.group.repository.IUsersRepository;
import etraveli.group.service.IUsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService implements IUsersService {

    private final IUsersRepository userRepository;

    private final IRoleRepository roleRepository;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(IUsersRepository userRepository, IRoleRepository roleRepository,
                        ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserListDto getAllUsers() {
        return UserListDto.builder()
                .users(userRepository.findAll().stream()
                        .map(user -> UsersDto.builder()
                                .id(user.getIdUser())
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .roles(user.getUsersRoles().stream()
                                        .map(userRol -> RoleDto.builder()
                                                .id(userRol.getRole().getIdRole())
                                                .name(userRol.getRole().getName())
                                                .description(userRol.getRole().getDescription())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public UsersDto createAdministrator(UserRequestDto userRequestDto) {
        Optional<Role> optionalRole = roleRepository.findByName(ADMIN);

        if (optionalRole.isEmpty()) {
            throw new NotFoundException(EXCEPTION_ADMINISTRATOR_ROLE_NOT_FOUND);
        }

        // Avoid messages that include information of database DataIntegrityViolationException.
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            throw new BadRequestException(String.format(
                    EXCEPTION_ALREADY_EXIST_USER_WITH_USERNAME,
                    userRequestDto.getUsername()));
        }

        Users user = new Users();
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        // Set data role
        UsersRole userRol = new UsersRole();
        userRol.setRole(optionalRole.get());
        userRol.setUser(user);

        // Assign role to user
        user.setUsersRoles(List.of(userRol));

        user = userRepository.save(user);

        return UsersDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(List.of(modelMapper.map(user.getUsersRoles().getFirst().getRole(), RoleDto.class)))
                .build();
    }
}
