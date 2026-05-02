package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.*;
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
public class ControlVenda {
  @Autowired
  private RepositoryItemVenda repositoryItemVenda;
  @Autowired
  private RepositoryVenda repositoryVenda;
  @Autowired
  private RepositoryProduto repositoryProduto;
  @Autowired
  private RepositoryColaborador repositoryColaborador;
  @Autowired
  private RepositoryCliente repositoryCliente;

  private List<ItemVenda> listaItemVenda = new ArrayList<ItemVenda>();

  @GetMapping("/cadastroVenda")
  public ModelAndView cadastrar(Venda venda, ItemVenda itemVenda) {
    ModelAndView mv = new ModelAndView("rota");
    mv.addObject("venda", venda);
    mv.addObject("itemVenda", itemVenda);
    mv.addObject("listaItemEntrada",this.listaItemVenda);
    mv.addObject("colaboradores",repositoryColaborador.findAll());
    mv.addObject("clientes",repositoryCliente.findAll());
    mv.addObject("produtos",repositoryProduto.findAll());
    return mv;
  }

 @GetMapping("/editarVenda/{id}")
  public ModelAndView editarVenda(@PathVariable Integer id) {
   Optional<Venda> venda = repositoryVenda.findById(id);
   this.listaItemVenda = repositoryItemVenda.findBVenda(venda.get().getId());
   return cadastrar(venda.get(), new ItemVenda());
  }

 // @GetMapping("/deletarEntrada/{id}")
 // public ModelAndView deletEntrada(@PathVariable Integer id) {
 //   Optional<Entrada> entrada = repositoryEntrada.findById(id);
 //   repositoryEntrada.deleteById(id);
 //   return listarEntrada();
 // }

  @GetMapping("/listarVenda")
  public ModelAndView listarVenda() {
    ModelAndView mv = new ModelAndView("/listarVenda");
    mv.addObject("vendas", repositoryVenda.findAll());
    return mv;
  }

  @PostMapping("/salvarVenda")
  public ModelAndView salvar (String acao,Venda venda, ItemVenda itemVenda, BindingResult result) {
    if (result.hasErrors()) {
      return cadastrar(venda, itemVenda);
    }

    if (acao.equals("itens")) {
      itemVenda.setValor(itemVenda.getProduto().getPreco());
      itemVenda.setSubtotal(itemVenda.getProduto().getPreco() * itemVenda.getQuantidade());
      venda.setValorTotal(venda.getValorTotal() + itemVenda.getValor());
      venda.setQuantidadeTotal(venda.getQuantidadeTotal() + itemVenda.getQuantidade());
      this.listaItemVenda.add(itemVenda);
    } else if (acao.equals("salvar")) {
      repositoryVenda.save(venda);

      for (ItemVenda it : listaItemVenda) {
        it.setVenda(venda);
        repositoryItemVenda.save(it);

        Produto produto = repositoryProduto.findById(it.getProduto().getId());
        produto.setQuantidade((int) (produto.getQuantidade() - it.getQuantidade()));
        produto.setPreco(it.getValor());
        //produto.setPrecoCusto(it.getValorCusto());
        repositoryProduto.save(produto);

        this.listaItemVenda = new ArrayList<>();

      }
      return cadastrar(new Venda(), new ItemVenda());
    }
    return cadastrar(venda, new ItemVenda());
  }

  public List<ItemVenda> getListaItemVenda() {
    return listaItemVenda;
  }

  public void setListaItemVenda(List<ItemVenda> listaItemVenda) {
    this.listaItemVenda = listaItemVenda;
  }
}
