package org.example.web.utils;

import spark.Response;

import static org.example.constants.Constants.*;

public class ResponseUtils {

    public static void setResponseDefaults(Response res, int status) {
        res.status(status);
        res.type(APP_JSON);
    }

    public static String returnErrorResponse(int status, String message, Response res) {
        res.status(status);
        res.type(APP_JSON);
        return String.format(ERROR_JSON, status, message);
    }
}
