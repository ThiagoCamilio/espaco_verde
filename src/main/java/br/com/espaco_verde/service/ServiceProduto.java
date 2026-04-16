package br.com.espaco_verde.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import br.com.espaco_verde.entity.Produto;
import br.com.espaco_verde.repository.RepositoryProduto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ServiceProduto{

    @Value("${dir.uploads}")
    private  String diretorioUpload;

    @Autowired
    private RepositoryProduto repositoryProduto;

    @Autowired
    private ServiceImage serviceImage;

    public ResponseEntity<?> register(Produto product, MultipartFile image) throws IOException {

        if(!image.isEmpty()){
            String imageName = serviceImage.saveImage(image);
            product.setImagem(imageName);
        }else {
            product.setImagem(repositoryProduto.findById(product.getId()).getImagem());
        }

        return new ResponseEntity<>(repositoryProduto.save(product), HttpStatus.CREATED);

    }

    public ResponseEntity<?> update(Produto product, MultipartFile image) throws IOException{

        String currentImage = repositoryProduto.findById(product.getId()).getImagem();
        product.setImagem(currentImage);

        if (!image.isEmpty()){
            String newImageName = serviceImage.updateImage(product, image);
            product.setImagem(newImageName);
        }else{
            product.setImagem(repositoryProduto.findById(product.getId()).getImagem());
        }

        return new ResponseEntity<>(repositoryProduto.save(product), HttpStatus.CREATED);
    }

    public List<Produto> listAll() throws Exception{
        return repositoryProduto.findAll();
    }

    public Produto findById(int id) {

        return repositoryProduto.findById(id);

    }
}