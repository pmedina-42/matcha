package org.example.web.config;

import org.example.database.DatabaseConnection;
import org.example.filter.AuthFilter;
import org.example.web.controller.*;
import org.example.web.socket.ChatSocketHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.example.constants.Constants.BACKEND_PORT;
import static org.example.constants.Constants.CHAT_PATH;
import static spark.Spark.*;

public class MatchaConfig {

    public MatchaConfig() throws IOException {
        initEnvVars();
        port(Integer.parseInt(initEnvVars().getProperty(BACKEND_PORT)));
        webSocket(CHAT_PATH, ChatSocketHandler.class);
        before(filter::filterAuthToken);
        initAllRoutes();
    }

    AuthFilter filter = new AuthFilter();
    UserController userController = new UserController();
    LogController logController = new LogController();
    LoginController loginController = new LoginController();
    ReportController reportController = new ReportController();
    ImageController imageController = new ImageController();

    private void initAllRoutes() {
        userController.initRoutes();
        logController.initRoutes();
        loginController.initRoutes();
        reportController.initRoutes();
        imageController.initRoutes();
    }

    private Properties initEnvVars() throws IOException {
        String appConfigPath = "/home/yop/Documentos/spark/matcha/back/backend/src/main/resources/app.properties";
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));
        DatabaseConnection.initializeDatabaseVariables(appProps);
        return appProps;
    }

}
