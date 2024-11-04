package org.example.service.impl;

import org.example.database.DatabaseConnection;
import org.example.database.DatabaseHelper;
import org.example.model.entity.Report;
import org.example.model.entity.User;
import org.example.service.ReportService;
import org.example.service.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Set;

import static org.example.enums.Role.ADMIN;

public class ReportServiceImpl implements ReportService {

    UserService userService = new UserServiceImpl();

    @Override
    public Report reportUser(Report report, String reporter) {
        try {
            if (!reporter.equals(report.getReporter())) {
                throw new RuntimeException("Users can only report on their name");
            }
            if (userService.getUserByField("userName", report.getReporter()).size() != 1
                    || userService.getUserByField("userName", report.getReported()).size() != 1) {
                throw new RuntimeException("Wrong number of users found");
            };
            report.setDate(new Timestamp(System.currentTimeMillis()));
            report.setSolved(false);
            Connection conn = DatabaseConnection.getConnection();
            DatabaseHelper.insertObject(conn, "reports", report);
            return report;
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void solveReport(Report report, String solver) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Set<User> users = DatabaseHelper.getObjectsByField(conn, "users", User.class, new String[]{"userName"}, new String[]{solver});
            if (users.size() != 1) {
                throw new RuntimeException("Wrong number of users");
            }
            User user = users.iterator().next();
            if (!user.getUserName().equals(solver)) {
                throw new RuntimeException("Something went horribly wrong");
            } else if (!ADMIN.equals(user.getRole())) {
                throw new RuntimeException("You don't have permissions to perform this action");
            }
            report = DatabaseHelper.getObjectIfExists(conn, "reports", Report.class, new String[]{"id"}, new String[]{String.valueOf(report.getId())});
            report.setSolved(true);
            DatabaseHelper.updateObject(conn, "reports", report, "id", String.valueOf(report.getId()));
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Report> getReports() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            return DatabaseHelper.getAllObjects(conn, "reports", Report.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
