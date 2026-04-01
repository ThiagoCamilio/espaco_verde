package br.com.espaco_verde.control;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.espaco_verde.entity.Produto;
import br.com.espaco_verde.entity.TiposProdutos;
import br.com.espaco_verde.repository.RepositoryProduto;
import br.com.espaco_verde.service.ServiceProduto;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/produtos")
public class ControlProdutos {

    @Autowired
    private RepositoryProduto acaoProduto;

    @Autowired
    private ServiceProduto serviceProduto;

    @Value("${dir.uploads}")
    private  String diretorioUpload;

    @GetMapping("")
    public String mensagem(HttpSession session){

        return "PAGINA INICIAL";

    }

    @GetMapping("/mock")
    public void mock() {

        Produto produto1 = new Produto( "Margarida", TiposProdutos.OUTRO, 2, "23/03/2026", 2.1, 3.1);
        acaoProduto.save(produto1);

    }

    @PostMapping("/cadastroProduto")
    public ResponseEntity<?> cadastroProduto(@RequestPart("dados") Produto p, @RequestPart("imagem") MultipartFile imagem) {

        try {
            serviceProduto.cadastrar(p, imagem);
            return ResponseEntity.status(200).body("Produto salvo com sucesso");
        } catch (IIOException e){
            return ResponseEntity.status(500).body("Erro ao salvar o produto");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/imagem/{nomeImagem:.+}")
    public ResponseEntity<Resource> exibirImagem(@PathVariable String nomeImagem){

        try {

            Path caminho = Paths.get(diretorioUpload).resolve(nomeImagem);
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
}
