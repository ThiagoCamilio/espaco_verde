package br.com.espaco_verde.service;

import br.com.espaco_verde.entity.Produto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ServiceImage {

    @Value("${dir.uploads}")
    private  String uploadDirectory;

    public Path getDirectory() throws IOException {

        Path directory = Paths.get(uploadDirectory);
        if (!Files.exists(directory)){
            Files.createDirectory(directory);
        }

        return directory;

    }

    public String saveImage(MultipartFile image) throws IOException {

        Path directory = this.getDirectory();

        String imageName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path imagePath = directory.resolve(imageName);

        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        return imageName;

    }

    public String updateImage(Produto product, MultipartFile image) throws IOException {

        Path directory = this.getDirectory();
        Path currentImagePath = directory.resolve(product.getImagem());
        String newImagePath = saveImage(image);
        this.deleteImage(product);

        return newImagePath;

    }

    public void deleteImage(Produto product) throws IOException{

        Path directory = this.getDirectory();
        Path imagePath = directory.resolve(product.getImagem());
        Files.deleteIfExists(imagePath);

    }

}
