package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.Cidade;
import br.com.espaco_verde.entity.Entrada;
import br.com.espaco_verde.entity.ItemEntrada;
import br.com.espaco_verde.entity.Produto;
import br.com.espaco_verde.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ControlEntrada {
  @Autowired
  private RepositoryItemEntrada repositoryItemEntrada;
  @Autowired
  private RepositoryEntrada repositoryEntrada;
  @Autowired
  private RepositoryProduto repositoryProduto;
  @Autowired
  private RepositoryColaborador repositoryColaborador;
  @Autowired
  private RepositoryFornecedor repositoryFornecedor;

  private List<ItemEntrada> listaItemEntrada = new ArrayList<ItemEntrada>();

  @GetMapping("/cadastroEntrada")
  public ModelAndView cadastrar(Entrada entrada, ItemEntrada itemEntrada) {
    ModelAndView mv = new ModelAndView("rota");
    mv.addObject("entrada", entrada);
    mv.addObject("itemEntrada", itemEntrada);
    mv.addObject("listaItemEntrada",this.listaItemEntrada);
    mv.addObject("colaboradores",repositoryColaborador.findAll());
    mv.addObject("fornecedores",repositoryFornecedor.findAll());
    mv.addObject("produtos",repositoryProduto.findAll());
    return mv;
  }

 @GetMapping("/editarEntrada/{id}")
  public ModelAndView editarEntrada(@PathVariable Integer id) {
   Optional<Entrada> entrada = repositoryEntrada.findById(id);
   this.listaItemEntrada = repositoryItemEntrada.findByEntrada(entrada.get().getId());
   return cadastrar(entrada.get(), new ItemEntrada());
  }

 // @GetMapping("/deletarEntrada/{id}")
 // public ModelAndView deletEntrada(@PathVariable Integer id) {
 //   Optional<Entrada> entrada = repositoryEntrada.findById(id);
 //   repositoryEntrada.deleteById(id);
 //   return listarEntrada();
 // }

  @GetMapping("/listarEntrada")
  public ModelAndView listarEntrada() {
    ModelAndView mv = new ModelAndView("/listarEntrada");
    mv.addObject("entradas", repositoryEntrada.findAll());
    return mv;
  }

  @PostMapping("/salvarEntrada")
  public ModelAndView salvar (String acao,Entrada entrada, ItemEntrada itemEntrada, BindingResult result) {
    if (result.hasErrors()) {
      return cadastrar(entrada, itemEntrada);
    }

    if (acao.equals("itens")) {
      this.listaItemEntrada.add(itemEntrada);
      entrada.setValorTotal(entrada.getValorTotal() + itemEntrada.getValor());
      entrada.setQuantidadeTotal(entrada.getQuantidadeTotal() + itemEntrada.getQuantidade());
    } else if (acao.equals("salvar")) {
      repositoryEntrada.save(entrada);

      for (ItemEntrada it : listaItemEntrada) {
        it.setEntrada(entrada);
        repositoryItemEntrada.save(it);

        Produto produto = repositoryProduto.findById(it.getProduto().getId());
        produto.setQuantidade((int) (produto.getQuantidade() + it.getQuantidade()));
        produto.setPreco(it.getValor());
        produto.setPrecoCusto(it.getValorCusto());
        repositoryProduto.save(produto);

        this.listaItemEntrada = new ArrayList<>();

      }
      return cadastrar(new Entrada(), new ItemEntrada());
    }
    return cadastrar(entrada, new ItemEntrada());
  }

  public List<ItemEntrada> getListaItemEntrada() {
    return listaItemEntrada;
  }

  public void setListaItemEntrada(List<ItemEntrada> listaItemEntrada) {
    this.listaItemEntrada = listaItemEntrada;
  }
}
