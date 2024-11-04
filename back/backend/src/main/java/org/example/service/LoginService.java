package org.example.service;

import org.example.model.DTO.LoginDTO;
import org.example.model.DTO.SigninDTO;
import org.example.model.entity.User;

public interface LoginService {

    public User signin(SigninDTO signinDTO);

    public String login(LoginDTO loginDTO);

}
