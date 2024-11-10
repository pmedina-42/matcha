package org.example.web.controller;

import com.google.gson.Gson;
import org.example.service.ImageService;
import org.example.service.impl.ImageServiceImpl;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import static org.example.web.utils.ResponseUtils.setResponseDefaults;
import static spark.Spark.post;

public class ImageController {

    ImageService imageService = new ImageServiceImpl();

    public void initRoutes() {

        Gson gson = new Gson();

        post(("/images/:userName"), (req, res) -> {
            imageService.addImage(req);
            setResponseDefaults(res, 204);
            return "";
        });
    }
}
