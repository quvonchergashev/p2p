package com.example.p2ptransaction.controller;
import com.example.p2ptransaction.dto.LoginDto;
import com.example.p2ptransaction.dto.RegisterDto;
import com.example.p2ptransaction.payload.ResponseApi;
import com.example.p2ptransaction.service.AuthServiceImpl;
import com.example.p2ptransaction.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    private final UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        ResponseApi login = authService.login(loginDto);
        if (login.isSuccess()) return ResponseEntity.ok(login);
        return ResponseEntity.status(409).body(login);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto)throws IOException {
        ResponseApi responseApi = userService.registerUser(registerDto);
        if (responseApi.isSuccess()) return ResponseEntity.ok(responseApi);
        return ResponseEntity.status(409).body(responseApi);
    }
}
