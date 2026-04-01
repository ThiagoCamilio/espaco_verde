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
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceProduto{

    @Value("${dir.uploads}")
    private  String diretorioUpload;

    @Autowired
    private RepositoryProduto repositoryProduto;
    private String mensagem;

    public ResponseEntity<?> cadastrar(Produto produto, MultipartFile imagem) throws IOException {

        Path diretorio = Paths.get(diretorioUpload);
        if (!Files.exists(diretorio)){
            Files.createDirectory(diretorio);
        }

        String nomeImagem = UUID.randomUUID().toString() + "_" + imagem.getOriginalFilename();
        Path caminhoImagem = diretorio.resolve(nomeImagem);

        Files.copy(imagem.getInputStream(), caminhoImagem, StandardCopyOption.REPLACE_EXISTING);

        produto.setImagem(nomeImagem);

        return new ResponseEntity<>(repositoryProduto.save(produto), HttpStatus.CREATED);


    }

    public List<Produto> listarTodos() throws Exception{
        return repositoryProduto.findAll();
    }

    public Produto findById(int id) {

        return repositoryProduto.findById(id);

    }
}