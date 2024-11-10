package org.example.model.entity;

public class Block {
    private Integer id;
    private String blocker;
    private String blocked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBlocker() {
        return blocker;
    }

    public void setBlocker(String blocker) {
        this.blocker = blocker;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }
}
