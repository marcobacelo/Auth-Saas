package com.jwt.server.config;

import com.jwt.server.entities.Role;
import com.jwt.server.entities.User;
import com.jwt.server.repository.RoleRepository;
import com.jwt.server.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        final String ADMIN_ROLE = Role.Values.ADMIN.name();

        var userAdmin = userRepository.findByUsername(ADMIN_ROLE);
        var roleAdmin = roleRepository.findByName(ADMIN_ROLE);

        userAdmin.ifPresentOrElse(
                user -> System.out.println("'ADMIN' já existe"),
                () -> {
                    var user = new User();
                    user.setUsername(ADMIN_ROLE);
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );
    }
}
