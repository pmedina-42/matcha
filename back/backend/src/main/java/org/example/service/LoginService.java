package org.example.service;

import org.example.model.DTO.CreateUserResponseDTO;
import org.example.model.DTO.LoginDTO;
import org.example.model.DTO.SigninDTO;

public interface LoginService {

    public CreateUserResponseDTO signin(SigninDTO signinDTO);

    public String login(LoginDTO loginDTO);

}
