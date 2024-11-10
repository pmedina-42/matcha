package org.example.service.impl;

import com.google.gson.Gson;
import org.example.model.DTO.CreateUserResponseDTO;
import org.example.model.DTO.LoginDTO;
import org.example.model.DTO.SigninDTO;
import org.example.model.entity.User;
import org.example.service.LoginService;
import org.example.service.UserService;
import org.example.web.utils.JWTUtils;
import org.mindrot.jbcrypt.BCrypt;

import static org.example.enums.Role.USER;

public class LoginServiceImpl implements LoginService {

    Gson gson = new Gson();
    UserService userService = new UserServiceImpl();

    @Override
    public CreateUserResponseDTO signin(SigninDTO signinDTO) {
        if (!signinDTO.getPassword().equals(signinDTO.getConfirmPassword())) {
            throw new RuntimeException("Passwords dont match");
        }
        if (!signinDTO.getEmail().equals(signinDTO.getConfirmEmail())) {
            throw new RuntimeException("Emails dont match");
        }
        User newUser = gson.fromJson(gson.toJson(signinDTO), User.class);
        newUser.setRole(USER);
        return userService.addUser(newUser);
    }

    @Override
    public String login(LoginDTO loginDTO) {
        try {
            String searchField = loginDTO.getUser().contains("@") ? "email" : "userName";
            User user = userService.getUserByField(searchField, loginDTO.getUser());
            if (BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
                return JWTUtils.generateToken(user.getUserName(), user.getRole());
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
