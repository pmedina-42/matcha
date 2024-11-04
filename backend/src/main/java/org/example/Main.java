package org.example;

import org.example.filter.AuthFilter;
import org.example.web.controller.LogController;
import org.example.web.controller.LoginController;
import org.example.web.controller.ReportController;
import org.example.web.controller.UserController;
import org.example.web.socket.ChatSocketHandler;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        AuthFilter filter = new AuthFilter();
        port(8080);
        webSocket("/chat", ChatSocketHandler.class);
        before(filter::filterAuthToken);
        UserController userController = new UserController();
        LogController logController = new LogController();
        LoginController loginController = new LoginController();
        ReportController reportController = new ReportController();
        userController.initRoutes();
        logController.initRoutes();
        loginController.initRoutes();
        reportController.initRoutes();
    }
}