package etraveli.group.bootstrap;

import static etraveli.group.common.Constants.EXCEPTION_SUPER_ADMINISTRATOR_ROLE_NOT_FOUND;
import static etraveli.group.model.RoleEnum.SUPER_ADMIN;

import etraveli.group.dto.request.UserRequestDto;
import etraveli.group.exception.NotFoundException;
import etraveli.group.model.Role;
import etraveli.group.model.Users;
import etraveli.group.model.UsersRole;
import etraveli.group.repository.IRoleRepository;
import etraveli.group.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class SuperAdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        createSuperAdministrator();
    }

    private void createSuperAdministrator() {
        UserRequestDto userDto = new UserRequestDto();
        userDto.setUsername("superadmin");
        userDto.setPassword("123456");

        Optional<Role> optionalRole = roleRepository.findByName(SUPER_ADMIN);
        Optional<Users> optionalUsersSuperAdmin = usersRepository.findByUsername(userDto.getUsername());

        if (optionalRole.isEmpty()) {
            throw new NotFoundException(EXCEPTION_SUPER_ADMINISTRATOR_ROLE_NOT_FOUND);
        }
        if (optionalUsersSuperAdmin.isEmpty()) {
            Users user = new Users();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            // Set data role
            UsersRole userRole = new UsersRole();
            userRole.setRole(optionalRole.get());
            userRole.setUser(user);

            // Assign role to user
            user.setUsersRoles(List.of(userRole));

            usersRepository.save(user);
        }
    }
}
