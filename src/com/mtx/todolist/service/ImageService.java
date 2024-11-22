package com.mtx.todolist.service;

import com.mtx.todolist.util.PropertiesUtil;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ImageService {

    private static final ImageService INSTANCE = new ImageService();
    private static final String BASE_PATH = PropertiesUtil.get("image.base.url");
    private static final String DEFAULT_MALE_IMAGE = PropertiesUtil.get("image.male.default.url");
    private static final String DEFAULT_FEMALE_IMAGE = PropertiesUtil.get("image.female.default.url");

    @SneakyThrows
    public void upload(String imagePath, InputStream imageContent) {
        var imageFullPath = Path.of(BASE_PATH, imagePath);

        try (imageContent) {
            Files.createDirectories(imageFullPath.getParent());
            Files.write(imageFullPath, imageContent.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
    }

    @SneakyThrows
    public Optional<InputStream> getImage(String imagePath) {
        Path imageFullPath = Path.of(BASE_PATH, imagePath);
        return Files.isExecutable(imageFullPath)
                ? Optional.of(Files.newInputStream(imageFullPath))
                : Optional.empty();
    }

    public void delete(String imagePath) {
        if (imagePath.equals(DEFAULT_FEMALE_IMAGE) || imagePath.equals(DEFAULT_MALE_IMAGE)) {
            return;
        }
        var imageFullPath = Path.of(BASE_PATH, imagePath);
        if (Files.isExecutable(imageFullPath)) {
            try {
                Files.delete(imageFullPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ImageService getInstance() {
        return INSTANCE;
    }
}
