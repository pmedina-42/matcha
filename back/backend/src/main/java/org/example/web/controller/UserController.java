package org.example.web.controller;

import com.google.gson.Gson;
import org.example.model.entity.User;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;

import java.util.Set;
import java.util.UUID;

import static org.example.web.utils.ResponseUtils.returnErrorResponse;
import static org.example.web.utils.ResponseUtils.setResponseDefaults;
import static spark.Spark.get;
import static spark.Spark.put;


public class UserController {

    Gson gson = new Gson();
    UserService userService = new UserServiceImpl();

    public void initRoutes() {

        get("/users", (req, res) -> {
            Set<User> users = userService.getUsers();
            setResponseDefaults(res, !users.isEmpty() ? 200 : 204);
            return gson.toJson(users);
        });

        get("/users/:userName", (req, res) -> {
            Set<User> user = userService.getUserByField("userName", req.params("userName"));
            setResponseDefaults(res, !user.isEmpty() ? 200 : 204);
            return gson.toJson(user);
        });

        put("/users/:userName", (req, res) -> {
            try {
                User updatedUser = userService.updateUser(gson.fromJson(req.body(), User.class));
                setResponseDefaults(res, 201);
                return gson.toJson(updatedUser);
            } catch (Exception e) {
                return returnErrorResponse(400, e.getMessage(), res);
            }
        });

        get("/users/:userName/verify/:verificationToken", (req, res) -> {
           try {
               boolean isVerified = userService.verifyUser(req.params("userName"), UUID.fromString(req.params("verificationToken")));
               setResponseDefaults(res, 204);
               return gson.toJson(isVerified);
           } catch (Exception e) {
               return returnErrorResponse(400, e.getMessage(), res);
           }
        });

    }
}
