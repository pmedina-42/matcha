package org.example.web.controller;

import com.google.gson.Gson;
import org.example.model.entity.Report;
import org.example.service.ReportService;
import org.example.service.impl.ReportServiceImpl;

import java.util.Set;

import static org.example.web.utils.ResponseUtils.returnErrorResponse;
import static org.example.web.utils.ResponseUtils.setResponseDefaults;
import static spark.Spark.get;
import static spark.Spark.post;

public class ReportController {

    Gson gson = new Gson();
    ReportService reportService = new ReportServiceImpl();

    public void initRoutes() {


        get("/reports", (req, res) -> {
            try {
                Set<Report> reports = reportService.getReports();
                res.type("application/json");
                res.status(200);
                return gson.toJson(reports);

            } catch (Exception e) {
                return returnErrorResponse(400, e.getMessage(), res);
            }
        });

        post("/reports", (req, res) -> {
            try {
                String requesterUserName = req.attribute("username");
                Report report = reportService.reportUser(gson.fromJson(req.body(), Report.class), requesterUserName);
                res.type("application/json");
                res.status(202);
                return gson.toJson(report);
            } catch (Exception e) {
                return returnErrorResponse(400, e.getMessage(), res);
            }
        });

        post("/reports/solve", (req, res) -> {
            try {
                String solver = req.attribute("username");
                reportService.solveReport(gson.fromJson(req.body(), Report.class), solver);
                setResponseDefaults(res,204);
                return "";

            } catch (Exception e) {
                return returnErrorResponse(400, e.getMessage(), res);
            }
        });

    }
}
