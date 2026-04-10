package com.anilist.backend.server.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path uploadPath;

    @PostConstruct
    public void init() {
        uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de uploads", e);
        }
    }

    public String store(MultipartFile file) {
        validateFile(file);

        String extension = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + extension;

        try (InputStream inputStream = file.getInputStream()) {
            Path target = uploadPath.resolve(filename).normalize();

            if (!target.startsWith(uploadPath)) {
                throw new RuntimeException("Caminho de arquivo inválido");
            }

            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar o arquivo", e);
        }
    }

    public Resource load(String filename) {
        try {
            Path file = uploadPath.resolve(filename).normalize();

            if (!file.startsWith(uploadPath)) {
                throw new RuntimeException("Caminho de arquivo inválido");
            }

            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Arquivo não encontrado: " + filename);
            }
            return resource;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar o arquivo", e);
        }
    }

    public void delete(String filename) {
        try {
            Path file = uploadPath.resolve(filename).normalize();

            if (!file.startsWith(uploadPath)) {
                throw new RuntimeException("Caminho de arquivo inválido");
            }

            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao excluir o arquivo", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido. Use: JPEG, PNG, GIF ou WebP");
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
