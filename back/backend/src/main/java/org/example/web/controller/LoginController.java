package org.example.web.controller;

import com.google.gson.Gson;
import org.example.model.DTO.CreateUserResponseDTO;
import org.example.model.DTO.LoginDTO;
import org.example.model.DTO.SigninDTO;
import org.example.service.LoginService;
import org.example.service.impl.LoginServiceImpl;

import static org.example.constants.Constants.LOGIN_PATH;
import static org.example.constants.Constants.SIGNIN_PATH;
import static org.example.web.utils.ResponseUtils.returnErrorResponse;
import static org.example.web.utils.ResponseUtils.setResponseDefaults;
import static spark.Spark.post;

public class LoginController {

    LoginService loginService = new LoginServiceImpl();
    Gson gson = new Gson();

    public void initRoutes() {
        post(SIGNIN_PATH, (req, res) -> {
            try {
                CreateUserResponseDTO user = loginService.signin(gson.fromJson(req.body(), SigninDTO.class));
                setResponseDefaults(res, 201);
                return gson.toJson(user);
            } catch (Exception e) {
                return returnErrorResponse(400, e.getMessage(), res);
            }
        });
        post(LOGIN_PATH, (req, res) -> {
            try {
                String token = loginService.login(gson.fromJson(req.body(), LoginDTO.class));
                if (token != null) {
                    setResponseDefaults(res, 200);
                    return "{\"accessToken\": " + gson.toJson(token) + "}";
                }
                return returnErrorResponse(401, "Invalid credentials.", res);
            } catch (Exception e) {
                return returnErrorResponse(400, e.getMessage(), res);
            }
        });
    }
}
