package org.example.web.controller;

import com.google.gson.Gson;
import org.example.model.DTO.LoginDTO;
import org.example.model.DTO.SigninDTO;
import org.example.model.entity.User;
import org.example.service.LoginService;
import org.example.service.impl.LoginServiceImpl;

import static org.example.web.utils.ResponseUtils.returnErrorResponse;
import static org.example.web.utils.ResponseUtils.setResponseDefaults;
import static spark.Spark.post;

public class LoginController {

    LoginService loginService = new LoginServiceImpl();
    Gson gson = new Gson();

    public void initRoutes() {
        post("/signin", (req, res) -> {
            try {
                User user = loginService.signin(gson.fromJson(req.body(), SigninDTO.class));
                setResponseDefaults(res, 201);
                return gson.toJson(user);
            } catch (Exception e) {
                return returnErrorResponse(400, e.getMessage(), res);
            }
        });
        post("/login", (req, res) -> {
            try {
                String token = loginService.login(gson.fromJson(req.body(), LoginDTO.class));
                res.type("application/json");
                if (token != null) {
                    res.status(200);
                    return "{\"accessToken\": " + gson.toJson(token) + "}";
                } else {
                    res.status(401);
                    return "{\n\"errorMessage\": \"Unable to log in\"\n}";
                }
            } catch (Exception e) {
                return returnErrorResponse(400, e.getMessage(), res);
            }
        });
    }
}
