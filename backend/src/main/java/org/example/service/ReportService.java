package org.example.service;

import org.example.model.entity.Report;

import java.util.Set;

public interface ReportService {

    public Report reportUser(Report report, String reporter);

    public void solveReport(Report report, String solver);

    public Set<Report> getReports();

}
