package com.mtx.todolist.servlet;

import com.mtx.todolist.service.ImageService;
import com.mtx.todolist.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;

@WebServlet(UrlPath.IMAGES + "/*")
public class ImageServlet extends HttpServlet {

    private final ImageService imageService = ImageService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var requestURI = req.getRequestURI();
        var imagePath = requestURI.replace("/images", "");

        imageService.getImage(imagePath)
                .ifPresentOrElse(image -> {
                    resp.setContentType("application/octet-stream");
                    writeImage(image, resp);
                }, () -> resp.setStatus(500));
    }

    @SneakyThrows
    private void writeImage(InputStream image, HttpServletResponse resp) {
        try(image; var outputStream = resp.getOutputStream()) {
            int currentByte;
            while ((currentByte = image.read()) != -1) {
                outputStream.write(currentByte);
            }
        }
    }
}
