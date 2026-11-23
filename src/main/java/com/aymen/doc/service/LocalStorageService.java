package com.aymen.doc.service;

import com.aymen.doc.config.AppProperties;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

@Service
public class LocalStorageService {

    private final Path root;

    public LocalStorageService(AppProperties props) throws IOException {
        this.root =
                Path.of(props.storage().location())   // transforme le texte (valeur de location dans app.yaml) en objet Path
                        .toAbsolutePath() // recupere le chemin absolu
                        .normalize(); // supprime les redondnats
        Files.createDirectories(root);
    }

    public StoredFile store(MultipartFile file) throws IOException {

        String extension = extensionOf(file.getOriginalFilename());
        String storedName = UUID.randomUUID()  + extension;
        Path target = root.resolve(storedName).normalize();

        if(!target.startsWith(root)) {
            throw new IOException("Chemin de stockage invalide");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return new StoredFile(storedName,target.toString());
    }

    public RessourceData load(String storedName) throws IOException { // lire et charge le fichier

        Path target = root.resolve(storedName).normalize();
        if(!target.startsWith(root) || !Files.exists(target)) {
            throw new NoSuchFileException(storedName);
        }
        return new RessourceData(Files.readAllBytes(target),target);
    }

    public void delete(String storedName) throws IOException {
        Path target = root.resolve(storedName).normalize();
        if(target.startsWith(root)) Files.deleteIfExists(target);
    }

    private String extensionOf(@Nullable String name) {
        if(name == null || !name.contains(".")) return "";
        return name.substring(name.lastIndexOf(".")).toLowerCase();
    }


    public record StoredFile(String storedName,String path) {}
    public record RessourceData(byte[] bytes , Path path) {}


}
