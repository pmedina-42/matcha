package org.example.service.impl;

import com.google.gson.Gson;
import org.example.model.DTO.LoginDTO;
import org.example.model.DTO.SigninDTO;
import org.example.model.entity.User;
import org.example.service.LoginService;
import org.example.service.UserService;
import org.example.web.utils.JWTUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Set;

import static org.example.enums.Role.USER;

public class LoginServiceImpl implements LoginService {

    Gson gson = new Gson();
    UserService userService = new UserServiceImpl();

    @Override
    public User signin(SigninDTO signinDTO) {
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
            Set<User> users = userService.getUserByField(searchField, loginDTO.getUser());
            assert users != null;
            if (users.size() != 1) {
                throw  new RuntimeException("Wrong number of users found");
            }
            User user = new User();
            for (User value : users) {
                user = value;
            }
            if (BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
                return JWTUtils.generateToken(user.getUserName(), user.getRole());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
