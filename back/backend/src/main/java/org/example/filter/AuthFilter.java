package org.example.filter;

import org.example.web.utils.JWTUtils;
import spark.Request;
import spark.Response;

import static org.example.constants.Constants.UNAUTHORIZED;
import static org.example.web.utils.ResponseUtils.returnErrorResponse;
import static spark.Spark.halt;

public class AuthFilter {

    public void filterAuthToken(Request request, Response response) {
        if (request.pathInfo().equals("/login") || request.pathInfo().equals("/openapi.json") || request.pathInfo().equals("/signin")) {
            return;
        }
        String authHeader = request.headers("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            halt(401, returnErrorResponse(401, UNAUTHORIZED, response));
        }

        String token = authHeader.substring("Bearer ".length());
        try {
            String username = JWTUtils.getUsernameFromToken(token);
            if (username == null) {
                halt(401, returnErrorResponse(401, UNAUTHORIZED, response));
            }

            request.attribute("username", username);

        } catch (Exception e) {
            halt(401, returnErrorResponse(401, UNAUTHORIZED, response));
        }
    }
}
