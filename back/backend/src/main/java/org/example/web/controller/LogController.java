package org.example.web.controller;

import com.google.gson.Gson;
import org.example.model.entity.Log;
import org.example.service.LogService;
import org.example.service.impl.LogServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.example.web.utils.ResponseUtils.setResponseDefaults;
import static spark.Spark.get;
import static spark.Spark.post;

public class LogController {

    LogService logService = new LogServiceImpl();
    Gson gson = new Gson();

    public void initRoutes() {
        post("/logs", (req, res) -> {
            Log newLog = gson.fromJson(req.body(), Log.class);
            newLog = logService.createLog(newLog, req.attribute("username"));
            setResponseDefaults(res, 201);
            return gson.toJson(newLog);
        });

        get("/logs/:userName/:type", (req, res) -> {
            Set<Log> logs = logService.getLogsByUserName(req.params("userName"), Log.Type.valueOf(req.params("type")));
            setResponseDefaults(res, 200);
            return gson.toJson(logs);
        });

        get("/logs/:userName", (req, res) -> {
            Set<Log> logs = logService.getAllLogsByUserName(req.params("userName"));
            setResponseDefaults(res, 200);
            return gson.toJson(logs);
        });

        post("/logs/:userName/fameRate", (req, res) -> {
            float fameRate = logService.getUserFameRate(req.params("userName"));
            setResponseDefaults(res, 200);
            Map<String, Float> response = new HashMap<>();
            response.put("fameRate", fameRate);
           return gson.toJson(response);
        });

    }

}
