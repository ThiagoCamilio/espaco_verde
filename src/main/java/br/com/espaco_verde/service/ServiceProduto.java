package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.RegisterProductDTO;
import br.com.espaco_verde.DTO.ResponseProductDTO;
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

    public ResponseEntity<?> update(RegisterProductDTO productDTO, MultipartFile image) throws IOException{

        Produto product = productDTO.toEntity();

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

    public List<ResponseProductDTO> listAll() throws Exception{
        List<Produto> products = repositoryProduto.findAll();
        List<ResponseProductDTO> dtos = new ArrayList<>();
        for(Produto p : products){
            ResponseProductDTO dto = new ResponseProductDTO(p);
            dtos.add(dto);
        }

        return dtos;
    }

    public ResponseProductDTO listById(int id) {

        Produto product = repositoryProduto.findById(id);
        ResponseProductDTO dto = new ResponseProductDTO(product) ;
        return dto;

    }
}