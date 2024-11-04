package org.example.service.impl;

import org.example.database.DatabaseConnection;
import org.example.database.DatabaseHelper;
import org.example.model.entity.User;
import org.example.service.EmailService;
import org.example.service.UserService;
import org.example.service.utils.SearchUtils;
import org.mindrot.jbcrypt.BCrypt;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    EmailService emailService = new EmailServiceImpl();

    @Override
    public User addUser(User user) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            user.setVerificationToken(UUID.randomUUID());
            user.setPassword(hashPassword(user.getPassword()));
            //emailService.sendVerificationEmail(user);
            DatabaseHelper.insertObject(conn, "users", user);
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException("Failed to add user: " + e.getMessage());
        }
        return user;
    }

    @Override
    public Set<User> getUsers() {
        Set<User> users;
        try {
            Connection conn = DatabaseConnection.getConnection();
            users = DatabaseHelper.getAllObjects(conn, "users", User.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public Set<User> getUserByField(String fieldName, String fieldValue) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            return DatabaseHelper.getObjectsByField(conn, "users", User.class, new String[]{fieldName}, new String[]{fieldValue});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User updateUser(User user) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            return DatabaseHelper.updateObject(conn, "users", user, "userName", user.getUserName());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add user: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<User> getPossibleMatches(User user) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            User searchUser = new User();
            SearchUtils.defineSearchUserData(user, searchUser);
            return DatabaseHelper.getObjectsByField(conn, "users", User.class, new String[]{"userName"}, new String[]{user.getUserName()});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifyUser(String userName, UUID verificationToken) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Set<User> users = DatabaseHelper.getObjectsByField(conn, "users", User.class, new String[]{"userName"}, new String[]{userName});
            assert users != null;
            if (users.size() != 1) {
                throw new RuntimeException("Wrong number of users found");
            }
            for (Iterator<User> it = users.iterator(); it.hasNext(); ) {
                User user = it.next();
                if (user.getVerificationToken().equals(verificationToken)) {
                    user.setVerified(true);
                    updateUser(user);
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt()); // Hash the password with a generated salt
    }
}
