package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.RegisterProductDTO;
import br.com.espaco_verde.DTO.ProductDTO;
import br.com.espaco_verde.entity.PricingCategory;
import br.com.espaco_verde.entity.Product;
import br.com.espaco_verde.repository.RepositoryPricingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import br.com.espaco_verde.repository.RepositoryProduto;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    RepositoryPricingCategory repositoryPricingCategory;

    public ResponseEntity<?> register(RegisterProductDTO productDTO, MultipartFile image) throws IOException {

        Product product = productDTO.toEntity();

        if(!image.isEmpty()){
            String imageName = serviceImage.saveImage(image);
            product.setImagem(imageName);
        }else {
            product.setImagem(repositoryProduto.findById(product.getId()).orElseThrow(() -> new RuntimeException("Product não encontrado")).getImagem());
        }

        return new ResponseEntity<>(repositoryProduto.save(product), HttpStatus.CREATED);

    }

    public ResponseEntity<?> update(ProductDTO productDTO, MultipartFile image) throws IOException{

        Product product = productDTO.toEntity();
        product.setUseSuggestedPrice(false);
        if (image == null){
            String currentImage = repositoryProduto.findById(product.getId()).orElseThrow(() -> new RuntimeException("Product não encontrado")).getImagem();
            product.setImagem(currentImage);

        }else {
            String newImageName = serviceImage.updateImage(product, image);
            product.setImagem(newImageName);
        }

        return new ResponseEntity<>(repositoryProduto.save(product), HttpStatus.CREATED);
    }

    public List<ProductDTO> listAll() throws Exception{
        List<Product> products = repositoryProduto.findAll();
        List<ProductDTO> dtos = new ArrayList<>();
        for(Product p : products){
            ProductDTO dto = new ProductDTO(p);
            dtos.add(dto);
        }

        return dtos;
    }

    public ProductDTO listById(int id) {

        Product product = repositoryProduto.findById(id).orElseThrow(() -> new RuntimeException("Product não encontrado"));
        ProductDTO dto = new ProductDTO(product) ;
        return dto;

    }

    public List<ProductDTO> listActive (){
        List<Product> products = repositoryProduto.findActive();
        List<ProductDTO> productsDTOS = new ArrayList<>();
        for(Product p : products){
            ProductDTO dto = new ProductDTO(p);
            productsDTOS.add(dto);
        }

        return productsDTOS;

    }

    public ResponseEntity<?> delete(int id) {
        repositoryProduto.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public void updateSyncStatus(int id, boolean status) {

        Product product = repositoryProduto.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        product.setUseSuggestedPrice(status);

        if(status && product.getSuggestedPrice() != null){
            product.setPreco(product.getSuggestedPrice());
        }

        repositoryProduto.save(product);

    }

    @Transactional
    public void updatePricingCategory(int id, int categoryId) {

        Product product = repositoryProduto.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        PricingCategory pc = repositoryPricingCategory.findById(categoryId).orElseThrow(()-> new RuntimeException("Categoria de precificação não encontrada"));
        product.setPricingCategory(pc);
        repositoryProduto.save(product);

    }
}