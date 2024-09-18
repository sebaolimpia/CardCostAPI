package etraveli.group.bootstrap;

import static etraveli.group.model.RoleEnum.*;

import etraveli.group.model.Role;
import etraveli.group.model.RoleEnum;
import etraveli.group.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadRoles();
    }

    private void loadRoles() {
        RoleEnum[] roleNames = {SUPER_ADMIN, ADMIN, USER};
        Map<RoleEnum, String> roleDescriptions = Map.of(
                SUPER_ADMIN, "Super administrator role",
                ADMIN, "Administrator role",
                USER, "Default user role");

        Arrays.stream(roleNames).forEach(roleName -> {
            Optional<Role> optionalRol = roleRepository.findByName(roleName);
            optionalRol.ifPresentOrElse(
                    role -> System.out.println("Role " + role.getName() + " already exists, nothing to do."),
                    () -> {
                        Role role = Role.builder()
                                .name(roleName)
                                .description(roleDescriptions.get(roleName))
                                .build();
                        roleRepository.save(role);
                        System.out.println("Role " + role.getName() + " created.");
                    }
            );
        });
    }
}
