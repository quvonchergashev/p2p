package com.example.p2ptransaction.service;

import com.example.p2ptransaction.consts.RoleName;
import com.example.p2ptransaction.dto.RegisterDto;
import com.example.p2ptransaction.entity.Roles;
import com.example.p2ptransaction.entity.User;
import com.example.p2ptransaction.payload.ResponseApi;
import com.example.p2ptransaction.repositories.RoleRepository;
import com.example.p2ptransaction.repositories.UserRepository;
import com.example.p2ptransaction.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Override
    public ResponseApi registerUser(RegisterDto registerDto) {
        boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(registerDto.getPhoneNumber());
        if (existsByPhoneNumber) return new ResponseApi("User already have", false);
        if (!registerDto.getPassword().equals(registerDto.getPrePassword()))
            return new ResponseApi("Passwords don't match", false);
        Optional<Roles> roleName = roleRepository.findByRoleName(RoleName.ROLE_SERVICE);
        if (!roleName.isPresent()) return new ResponseApi("Role Not found", false);
        Roles roles = roleName.get();

        User user = new User();
        user.setUsername(registerDto.getFullName());
        user.setRoles(Collections.singletonList(roles));
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setEmail(registerDto.getEmail());
        userRepository.save(user);
        return new ResponseApi("Successfully registered", true);
    }
}
