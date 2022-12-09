package com.example.p2ptransaction.component;
import com.example.p2ptransaction.consts.RoleName;
import com.example.p2ptransaction.entity.EOPS;
import com.example.p2ptransaction.entity.Roles;
import com.example.p2ptransaction.entity.User;
import com.example.p2ptransaction.repositories.EOPSRepository;
import com.example.p2ptransaction.repositories.RoleRepository;
import com.example.p2ptransaction.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private String initialMode="always";

    private final PasswordEncoder passwordEncoder;

   private final UserRepository userRepository;

   private final EOPSRepository eopsRepository;

   private final RoleRepository roleRepository;



    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("")) {
            Roles admin=new Roles();
            admin.setRoleName(RoleName.ROLE_SERVICE);
            Roles save = roleRepository.save(admin);
            List<Roles> rolesList=new ArrayList<>();
            rolesList.add(save);

            User user1 = new User();
            user1.setPassword(passwordEncoder.encode("12345"));
            user1.setPhoneNumber("909008870");
            user1.setEmail("ergashevq346@gmail.com");
            user1.setUsername("Service");
            user1.setRoles(rolesList);
            userRepository.save(user1);

            EOPS eops=new EOPS();
            eops.setShotNumber("p2pTransaction");
            eopsRepository.save(eops);

        }

    }
}
