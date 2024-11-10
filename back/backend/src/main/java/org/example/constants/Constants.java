package org.example.constants;

public class Constants {

    /** Responses */

    public static final String APP_JSON = "application/json";
    public static final String ERROR_JSON = "{\"errorCode\": %d, \"reason\": \"%s\"}";
    public static final String AUTH_HEADER = "Authorization";
    public static final String UNAUTHORIZED = "Unauthorized";

    /** Properties */

    public static final String DB_URL = "DB_URL";
    public static final String DB_USER = "DB_USER";
    public static final String DB_PASS = "DB_PASS";
    public static final String BACKEND_PORT = "BACKEND_PORT";

    /** Routes */

    public static final String USERS_PATH = "/users";
    public static final String LOGS_PATH = "/logs";
    public static final String LOGIN_PATH = "/login";
    public static final String SIGNIN_PATH = "/signin";
    public static final String IMAGES_PATH = "/images";
    public static final String REPORTS_PATH = "/reports";
    public static final String CHAT_PATH = "/chat";

    /** Tables */

    public static final String USERS_TABLE = "users";
    public static final String LOGS_TABLE = "logs";
    public static final String REPORTS_TABLE = "reports";
    public static final String IMAGES_TABLE = "images";
    public static final String MATCHES_TABLE = "matches";
    public static final String MESSAGES_TABLE = "messages";
    public static final String BLOCKS_TABLE = "blocks";

}
