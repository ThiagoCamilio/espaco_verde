package br.com.espaco_verde.control;

import br.com.espaco_verde.DTO.RegisterProductDTO;
import br.com.espaco_verde.DTO.ProductDTO;
import br.com.espaco_verde.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.espaco_verde.repository.RepositoryProduto;
import br.com.espaco_verde.service.ServiceProduto;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/produtos")
public class ControlProdutos {

    @Autowired
    private RepositoryProduto acaoProduto;

    @Autowired
    private ServiceProduto serviceProduto;

    @Value("${dir.uploads}")
    private  String diretorioUpload;

    @GetMapping("/active")
    public ResponseEntity<List<ProductDTO>> listAllActive() throws Exception {

        List<ProductDTO> products = serviceProduto.listActive();
        return ResponseEntity.status(200).body(products);

    }

    @GetMapping("")
    public ResponseEntity<List<ProductDTO>> listAll() throws Exception {

        List<ProductDTO> products = serviceProduto.listAll();
        return ResponseEntity.status(200).body(products);

    }


    @GetMapping("/{id}")
    public ProductDTO listById(@PathVariable int id) throws Exception {

        return serviceProduto.listById(id);

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestPart("product") RegisterProductDTO p, @RequestPart("imagem") MultipartFile imagem) {

        try {
            return serviceProduto.register(p, imagem);
        } catch (IIOException e){
            return ResponseEntity.status(500).body("Erro ao salvar o produto");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/imagem/{imageName:.+}")
    public ResponseEntity<Resource> showImage(@PathVariable String imageName){

        try {
            Path caminho = Paths.get(diretorioUpload).resolve(imageName);
            Resource recurso = new UrlResource(caminho.toUri());

            if(recurso.exists() || recurso.isReadable()){
                String contentType = Files.probeContentType(caminho);
                if (contentType == null){
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(recurso);
            }else {
                return ResponseEntity.notFound().build();
            }

        }catch (Exception e){

            return ResponseEntity.internalServerError().build();
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestPart("product") ProductDTO p, @RequestPart(value = "imagem", required = false) MultipartFile imagem){
        try {
            return serviceProduto.update(p, imagem);
        } catch (IIOException e){
            return ResponseEntity.status(500).body("Erro ao salvar o produto");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Void> toggleStatus(@PathVariable int id){
        serviceProduto.toggleStatus(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        return serviceProduto.delete(id);

    }

    @PatchMapping("/{id}/sync-status")
    public ResponseEntity<?> updateSyncStatus(@PathVariable int id, @RequestParam boolean status){
        serviceProduto.updateSyncStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/category")
    public ResponseEntity<?> updatePricingCategory(@PathVariable int id, @RequestParam int categoryId){
        serviceProduto.updatePricingCategory(id, categoryId);
        return ResponseEntity.ok().build();
    }
}
