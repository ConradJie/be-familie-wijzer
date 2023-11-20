package com.jie.befamiliewijzer.services;

import com.jie.befamiliewijzer.models.Media;
import com.jie.befamiliewijzer.repositories.MediaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class MediaService {
    private final Path fileStoragePath;
    private final String fileStorageLocation;
    private final MediaRepository mediaRepository;

    public MediaService(@Value("${my.upload_location}") String fileStorageLocation, MediaRepository mediaRepository) {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        this.fileStorageLocation = fileStorageLocation;
        this.mediaRepository = mediaRepository;

        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }

    public String storeFile(MultipartFile file, String url, String prefix) {
        String fileName = String.format("%s%s", prefix, StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));

        Path filePath = Paths.get(String.format("%s\\%s", fileStoragePath, fileName));

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }

        mediaRepository.save(new Media(fileName, file.getContentType(), url));
        return fileName;
    }

    public Resource downLoadFile(String fileName) {
        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file", e);
        }

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("the file is not available");
        }
    }

}
