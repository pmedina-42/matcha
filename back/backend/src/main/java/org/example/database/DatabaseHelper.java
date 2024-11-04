package org.example.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DatabaseHelper {

    public static <T> int insertObject(Connection conn, String tableName, T object) throws SQLException, IllegalAccessException {
        Class<?> objClass = object.getClass();
        Field[] fields = Arrays.stream(objClass.getDeclaredFields())
                .filter(f -> !f.getName().equals("id"))
                .toArray(Field[]::new);

        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            columns.append("\"").append(fields[i].getName()).append("\"");
            placeholders.append("?");
            if (i < fields.length - 1) {
                columns.append(", ");
                placeholders.append(", ");
            }
        }
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < fields.length; i++) {
                Object value = fields[i].get(object);
                if (value instanceof Enum) {
                    stmt.setObject(i + 1, value.toString(), Types.VARCHAR);
                } else {
                    stmt.setObject(i + 1, value);
                }
            }
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating object failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            String cause = "";
            if (e.getMessage().contains("email")) {
                cause = "An account with that email already exists";
            } else if (e.getMessage().contains("userName")) {
                cause = "A user with that userName already exists";
            }
            throw new SQLException(cause);
        }
    }

    public static <T> T updateObject(Connection conn, String tableName, T object, String idField, String idValue) throws SQLException, IllegalAccessException {
        Class<?> objClass = object.getClass();
        Field[] fields = Arrays.stream(objClass.getDeclaredFields())
                .filter(f -> !f.getName().equals("id"))
                .toArray(Field[]::new);

        StringBuilder columns = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            columns.append("\"").append(fields[i].getName()).append("\"");
            columns.append(" = ? ");
            if (i < fields.length - 1) {
                columns.append(", ");
            }
        }
        String sql = "UPDATE " + tableName + " SET " + columns + " WHERE \"" + idField + "\" = '" + idValue + "'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < fields.length; i++) {
                Object value = fields[i].get(object);
                if (value instanceof Enum) {
                    stmt.setObject(i + 1, value.toString(), Types.VARCHAR);
                } else {
                    stmt.setObject(i + 1, value);
                }
            }
            stmt.executeUpdate();
            return object;
        } catch (Exception e) {
            String cause = "";
            if (e.getMessage().contains("email")) {
                cause = "An account with that email already exists";
            } else if (e.getMessage().contains("userName")) {
                cause = "A user with that userName already exists";
            }
            throw new SQLException(cause);
        }
    }

    public static <T> Set<T> getAllObjects(Connection conn, String tableName, Class<T> clazz) throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        Set<T> response = new HashSet<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet queryResponse = stmt.executeQuery();

            Constructor<T> constructor = clazz.getDeclaredConstructor();
            while (queryResponse.next()) {
                T obj = constructor.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = queryResponse.getObject(field.getName());
                    if (value != null) {
                        if (field.getType().isEnum()) {
                            value = Enum.valueOf((Class<Enum>) field.getType(), value.toString());
                        }
                        field.set(obj, value);
                    }
                }
                response.add(obj);
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static <T> Set<T> getObjectsByField(Connection conn, String tableName, Class<T> clazz, String[] fieldNames, String[] fieldValues) throws SQLException {

        if (fieldNames.length != fieldValues.length) {
            throw new RuntimeException("WTF Something was very wrong");
        }
        StringBuilder whereClause = new StringBuilder(" WHERE ");
        for (int i = 0; i < fieldNames.length; i++) {
            whereClause.append("\"").append(fieldNames[i]).append("\" = '").append(fieldValues[i]).append("'");
            if (i + 1 < fieldNames.length) {
                whereClause.append(" AND ");
            }
        }

        String sql = "SELECT * FROM " + tableName + whereClause;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet queryResponse = stmt.executeQuery();

            Constructor<T> constructor = clazz.getDeclaredConstructor();
            Set<T> response = new HashSet<>();
            while (queryResponse.next()) {
                T obj = constructor.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = queryResponse.getObject(field.getName());
                    if (value != null) {
                        if (field.getType().isEnum()) {
                            value = Enum.valueOf((Class<Enum>) field.getType(), value.toString());
                        }
                        field.set(obj, value);
                    }
                }
                response.add(obj);
            }
            return response;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Float getUserFameRate(Connection conn, String tableName, String userName) {
        String sql = "SELECT \n" +
                "    COUNT(CASE WHEN type = 'VIEW' THEN 1 END) AS view_count,\n" +
                "    COUNT(CASE WHEN type = 'LIKE' THEN 1 END) AS like_count\n" +
                "FROM logs\n" +
                "WHERE receiver = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);  // Set the receiver parameter

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    float viewCount = rs.getInt("view_count");
                    float likeCount = rs.getInt("like_count");
                    return likeCount/viewCount;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.f;
    }

    public static <T> T getObjectIfExists(Connection conn, String tableName, Class<T> clazz, String[] fieldNames, String[] fieldValues) throws SQLException {
        Set<T> response = getObjectsByField(conn, tableName, clazz, fieldNames, fieldValues);
        if (response == null || response.size() != 1 && !response.iterator().hasNext()) {
            throw new RuntimeException();
        }
        return response.iterator().next();
    }


}