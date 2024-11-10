package org.example.model.entity;

import java.util.UUID;

public class Image {
    private Integer id;
    private UUID locator;
    private String user;
    private boolean isProfilePic;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isProfilePic() {
        return isProfilePic;
    }

    public void setProfilePic(boolean profilePic) {
        isProfilePic = profilePic;
    }

    public UUID getLocator() {
        return locator;
    }

    public void setLocator(UUID locator) {
        this.locator = locator;
    }
}
