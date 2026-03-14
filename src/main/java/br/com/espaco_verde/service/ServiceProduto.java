package br.com.espaco_verde.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import br.com.espaco_verde.entity.Produto;
import br.com.espaco_verde.repository.RepositoryProduto;

@Service
public class ServiceProduto{

    @Autowired
    private RepositoryProduto repo;
    private String mensagem;

    public ResponseEntity<?> cadastrar(Produto produto){

        if(produto.getNome().equals("")){

            mensagem = "Por favor insira um nome para o produto"; 
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);

        }else if (produto.getPrecoCusto() <= 0){
            
            mensagem = "Por favor insira um preço de custo valido para o produto";
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);

        }else if (produto.getPreco() < produto.getPrecoCusto()){
            
            mensagem = "Por favor insira um preço de custo valido para o produto";
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);    

        }else if (produto.getQuantidade() <= 0){
            
            mensagem = "Por favor insira uma valida para o produto";
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);    

        }else{
            return new ResponseEntity<>(repo.save(produto), HttpStatus.CREATED);
        }

    }

    public Produto findById(int id) {

        return repo.findById(id);

    }
}