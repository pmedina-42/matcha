package org.example;

import org.example.web.config.MatchaConfig;

public class Matcha {
    public static void main(String[] args) {
        try {
            new MatchaConfig();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong");
        }
    }
}