package com.example.p2ptransaction.service.interfaces;

import com.example.p2ptransaction.dto.RegisterDto;
import com.example.p2ptransaction.payload.ResponseApi;

public interface UserService {

    ResponseApi registerUser(RegisterDto userDto);

}
