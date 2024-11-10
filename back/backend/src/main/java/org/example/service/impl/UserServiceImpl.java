package org.example.service.impl;

import org.example.database.DatabaseConnection;
import org.example.database.DatabaseHelper;
import org.example.model.DTO.CreateUserResponseDTO;
import org.example.model.entity.Block;
import org.example.model.entity.User;
import org.example.service.EmailService;
import org.example.service.UserService;
import org.example.service.utils.SearchUtils;
import org.example.web.utils.JWTUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

import static org.example.constants.Constants.USERS_TABLE;
import static org.example.enums.Role.USER;

public class UserServiceImpl implements UserService {

    EmailService emailService = new EmailServiceImpl();

    @Override
    public CreateUserResponseDTO addUser(User user) {
        CreateUserResponseDTO userResponse = new CreateUserResponseDTO();
        try {
            Connection conn = DatabaseConnection.getConnection();
            user.setRole(USER);
            user.setVerificationToken(UUID.randomUUID());
            user.setPassword(hashPassword(user.getPassword()));
            DatabaseHelper.insertObject(conn, USERS_TABLE, user);
            userResponse.setUserName(user.getUserName());
            userResponse.setEmail(user.getEmail());
            userResponse.setName(user.getName());
            userResponse.setLastName(user.getLastName());
            userResponse.setVerified(user.isVerified());
            userResponse.setAccessToken(JWTUtils.generateToken(user.getUserName(), user.getRole()));
            //emailService.sendVerificationEmail(user);
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException("Failed to add user: " + e.getMessage());
        }
        return userResponse;
    }

    @Override
    public Set<User> getUsers() {
        Set<User> users;
        try {
            Connection conn = DatabaseConnection.getConnection();
            users = DatabaseHelper.getAllObjects(conn, USERS_TABLE, User.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public User getUserByField(String fieldName, String fieldValue) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            return DatabaseHelper.getObjectByFields(conn, USERS_TABLE, User.class, new String[]{fieldName}, new String[]{fieldValue});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User updateUser(User user) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            return DatabaseHelper.updateObject(conn, USERS_TABLE, user, "userName", user.getUserName());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add user: " + e.getMessage());
        }
    }

    @Override
    public Set<User> getPossibleMatches(User user) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            User searchUser = new User();
            SearchUtils.defineSearchUserData(user, searchUser);
            return DatabaseHelper.getObjectsByField(conn, USERS_TABLE, User.class, new String[]{"userName"}, new String[]{user.getUserName()});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifyUser(String userName, UUID verificationToken) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            User user = DatabaseHelper.getObjectByFields(conn, USERS_TABLE, User.class, new String[]{"userName"}, new String[]{userName});
            assert user != null;
            if (user.getVerificationToken().equals(verificationToken)) {
                user.setVerified(true);
                updateUser(user);
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean blockUser(String blocker, String blocked) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            User blockerUser = DatabaseHelper.getObjectIfExists(conn, USERS_TABLE, User.class, new String[]{"userName"}, new String[]{blocker});
            User blockedUser = DatabaseHelper.getObjectIfExists(conn, USERS_TABLE, User.class, new String[]{"userName"}, new String[]{blocker});
            Block block = new Block();
            block.setBlocker(blocker);
            block.setBlocked(blocked);
            DatabaseHelper.insertObject(conn, "blocks", block);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
