package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.dtos.LoginRequestDto;
import com.luizgmelo.conduit.dtos.RegisterRequestDto;
import com.luizgmelo.conduit.dtos.ResponseUserDto;

public interface IAuthService {

  ResponseUserDto login(LoginRequestDto body);

  ResponseUserDto register(RegisterRequestDto body);

}
