package org.example.service.impl;

import org.example.database.DatabaseConnection;
import org.example.database.DatabaseHelper;
import org.example.model.entity.Log;
import org.example.service.LogService;
import org.example.service.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class LogServiceImpl implements LogService {

    UserService userService = new UserServiceImpl();

    @Override
    public Log createLog(Log log) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            log.setDate(new Timestamp(System.currentTimeMillis()));
            Integer id = DatabaseHelper.insertObject(conn, "logs", log);
            log.setId(id);

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException("Failed to add log: " + e.getMessage());
        }
        return log;
    }

    @Override
    public Set<Log> getLogsByUserName(String userName, Log.Type type) {
        Set<Log> logs;
        try {
            Connection conn = DatabaseConnection.getConnection();
            logs = DatabaseHelper.getObjectsByField(conn, "logs", Log.class, new String[]{"receiver", "type"}, new String[]{userName, type.name()});
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retreive logs: " + e.getMessage());
        }
        return logs;
    }

    @Override
    public Set<Log> getAllLogsByUserName(String userName) {
        Set<Log> logs;
        try {
            Connection conn = DatabaseConnection.getConnection();
            logs = DatabaseHelper.getObjectsByField(conn, "logs", Log.class, new String[]{"receiver"}, new String[]{userName});
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retreive logs: " + e.getMessage());
        }
        return logs;
    }

    @Override
    public Float getUserFameRate(String userName) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            return DatabaseHelper.getUserFameRate(conn, "logs", userName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
