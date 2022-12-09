package com.example.p2ptransaction.service.interfaces;


import com.example.p2ptransaction.dto.LoginDto;
import com.example.p2ptransaction.payload.ResponseApi;

public interface AuthService {

    public ResponseApi login(LoginDto loginDto);

}
