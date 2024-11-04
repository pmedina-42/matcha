package org.example.service;

import org.example.model.entity.User;

import java.util.Set;
import java.util.UUID;

public interface UserService {

    public User addUser(User user);

    public Set<User> getUsers();

    public Set<User> getUserByField(String fieldName, String fieldValue);

    public User updateUser(User user);

    public Set<User> getPossibleMatches(User user);

    public boolean verifyUser(String userName, UUID verificationToken);
}