package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.Cliente;
import br.com.espaco_verde.entity.Fornecedor;
import br.com.espaco_verde.repository.RepositoryCidade;
import br.com.espaco_verde.repository.RepositoryCliente;
import br.com.espaco_verde.repository.RepositoryFornecedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ControlFornecedor {

  @Autowired
  private RepositoryCidade repositoryCidade;

  @Autowired
  private RepositoryFornecedor repositoryFornecedor;

  @GetMapping("/cadastroFornecedor")
  public ModelAndView cadastroFornecedor(Fornecedor fornecedor) {

    ModelAndView mv = new ModelAndView("rota");
    mv.addObject("fornecedor", fornecedor);
    mv.addObject("listaCidades", repositoryCidade.findAll());
    return mv;
  }

  @GetMapping("/editarFornecedor/{id}")
  public ModelAndView editarFornecedor(@PathVariable Integer id) {
    Optional<Fornecedor> fornecedor = repositoryFornecedor.findById(id);
    return cadastroFornecedor(fornecedor.get());
  }

  @GetMapping("/deletarFornecedor/{id}")
  public ModelAndView deletFornecedor(@PathVariable Integer id) {
    Optional<Fornecedor> fornecedor = repositoryFornecedor.findById(id);
    repositoryFornecedor.deleteById(id);
    return listarFornecedor();
  }

  @GetMapping("/listarFornecedor")
  public ModelAndView listarFornecedor() {
    ModelAndView mv = new ModelAndView("/listarFornecedor");
    mv.addObject("fornecedores", repositoryFornecedor.findAll());
    return mv;
  }

  @PostMapping("/salvarFornecedor")
  public ModelAndView salvarFornecedor(Fornecedor fornecedor, BindingResult result) {
    if (result.hasErrors()) {
      return cadastroFornecedor(fornecedor);
    }
    repositoryFornecedor.save(fornecedor);
    return cadastroFornecedor(new Fornecedor());

  }

}
