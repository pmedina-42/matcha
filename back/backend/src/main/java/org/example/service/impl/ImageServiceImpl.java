package org.example.service.impl;

import org.example.database.DatabaseConnection;
import org.example.database.DatabaseHelper;
import org.example.model.entity.Image;
import org.example.service.ImageService;
import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import static org.example.constants.Constants.IMAGES_TABLE;

public class ImageServiceImpl implements ImageService {
    @Override
    public void addImage(Request req) {
        try {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
            req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            Part file = req.raw().getPart("file");
            String userName = req.params("userName");
            Connection conn = DatabaseConnection.getConnection();
            Image image = new Image();
            image.setLocator(UUID.randomUUID());
            image.setUser(userName);
            File localFile = new File("/home/yop/Documentos/spark/matcha/back/backend/src/main/resources/images/" + image.getLocator());
            try (InputStream inputStream = file.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(localFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            DatabaseHelper.insertObject(conn, IMAGES_TABLE, image);
            req.raw().removeAttribute("org.eclipse.jetty.multipartConfig");
        } catch (SQLException | IllegalAccessException | ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
