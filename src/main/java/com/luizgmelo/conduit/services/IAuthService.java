package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.dtos.LoginRequestDto;
import com.luizgmelo.conduit.dtos.RegisterRequestDto;
import com.luizgmelo.conduit.dtos.ResponseRegisterLoginDto;

public interface IAuthService {

  ResponseRegisterLoginDto login(LoginRequestDto body);

  ResponseRegisterLoginDto register(RegisterRequestDto body);

}
