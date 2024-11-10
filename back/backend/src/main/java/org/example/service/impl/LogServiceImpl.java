package org.example.service.impl;

import org.example.database.DatabaseConnection;
import org.example.database.DatabaseHelper;
import org.example.model.entity.Log;
import org.example.model.entity.Match;
import org.example.service.LogService;
import org.example.service.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Set;

import static org.example.model.entity.Log.Type.LIKE;

public class LogServiceImpl implements LogService {

    UserService userService = new UserServiceImpl();

    @Override
    public Log createLog(Log log, String user) {
        try {
            if (log.getReceiver().equals(user)) {
                throw new RuntimeException("You cannot interact with yourself");
            }
            Connection conn = DatabaseConnection.getConnection();
            log.setDate(new Timestamp(System.currentTimeMillis()));
            log.setSender(user);
            DatabaseHelper.insertObject(conn, "logs", log);
            // TODO notificar al receiver de la nueva visita o like
            if (log.getType().equals(LIKE)) {
                tryToCreateMatch(conn, user, log.getReceiver());
            }
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

    private void createLike() {

    }

    private void tryToCreateMatch(Connection conn, String user1, String user2) throws SQLException, IllegalAccessException {
        try {
            if (DatabaseHelper.getObjectByFields(conn, "logs", Log.class, new String[]{"sender", "receiver"}, new String[]{user2, user1}) != null) {
                Match match = new Match();
                match.setUser1(user2);
                match.setUser2(user1);
                match.setDate(new Timestamp(System.currentTimeMillis()));
                DatabaseHelper.insertObject(conn, "matches", match);
                // TODO probablemente enviar evento por websocket para avisar de notificacion al otro si esta conectado
            }
        } catch (Exception _) {
        }
    }
}
