package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.RegisterProductDTO;
import br.com.espaco_verde.DTO.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import br.com.espaco_verde.entity.Produto;
import br.com.espaco_verde.repository.RepositoryProduto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceProduto{

    @Value("${dir.uploads}")
    private  String diretorioUpload;

    @Autowired
    private RepositoryProduto repositoryProduto;

    @Autowired
    private ServiceImage serviceImage;

    public ResponseEntity<?> register(RegisterProductDTO productDTO, MultipartFile image) throws IOException {

        Produto product = productDTO.toEntity();

        if(!image.isEmpty()){
            String imageName = serviceImage.saveImage(image);
            product.setImagem(imageName);
        }else {
            product.setImagem(repositoryProduto.findById(product.getId()).getImagem());
        }

        return new ResponseEntity<>(repositoryProduto.save(product), HttpStatus.CREATED);

    }

    public ResponseEntity<?> update(ProductDTO productDTO, MultipartFile image) throws IOException{

        System.out.println(productDTO.id());
        Produto product = productDTO.toEntity();

        if (image == null){
            String currentImage = repositoryProduto.findById(product.getId()).getImagem();
            product.setImagem(currentImage);

        }else {
            String newImageName = serviceImage.updateImage(product, image);
            product.setImagem(newImageName);
        }

        return new ResponseEntity<>(repositoryProduto.save(product), HttpStatus.CREATED);
    }

    public List<ProductDTO> listAll() throws Exception{
        List<Produto> products = repositoryProduto.findAll();
        List<ProductDTO> dtos = new ArrayList<>();
        for(Produto p : products){
            ProductDTO dto = new ProductDTO(p);
            dtos.add(dto);
        }

        return dtos;
    }

    public ProductDTO listById(int id) {

        Produto product = repositoryProduto.findById(id);
        ProductDTO dto = new ProductDTO(product) ;
        return dto;

    }

    public ResponseEntity<?> delete(int id) {
        repositoryProduto.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}