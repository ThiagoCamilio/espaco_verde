package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.Cliente;
import br.com.espaco_verde.entity.Colaborador;
import br.com.espaco_verde.repository.RepositoryCidade;
import br.com.espaco_verde.repository.RepositoryCliente;
import br.com.espaco_verde.repository.RepositoryColaborador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ControlCliente {

  @Autowired
  private RepositoryCidade repositoryCidade;

  @Autowired
  private RepositoryCliente repositoryCliente;

  @GetMapping("/cadastroCliente")
  public ModelAndView cadastroCliente(Cliente cliente) {

    ModelAndView mv = new ModelAndView("rota");
    mv.addObject("cliente", cliente);
    mv.addObject("listaCidades", repositoryCidade.findAll());
    return mv;
  }

  @GetMapping("/editarCliente/{id}")
  public ModelAndView editarCliente(@PathVariable Integer id) {
    Optional<Cliente> cliente = repositoryCliente.findById(id);
    return cadastroCliente(cliente.get());
  }

  @GetMapping("/deletarCliente/{id}")
  public ModelAndView deletCliente(@PathVariable Integer id) {
    Optional<Cliente> cliente = repositoryCliente.findById(id);
    repositoryCliente.deleteById(id);
    return listarCliente();
  }

  @GetMapping("/listarCliente")
  public ModelAndView listarCliente() {
    ModelAndView mv = new ModelAndView("/listarCliente");
    mv.addObject("clientes", repositoryCliente.findAll());
    return mv;
  }

  @PostMapping("/salvarCliente")
  public ModelAndView salvarColaborador(Cliente cliente, BindingResult result) {
    if (result.hasErrors()) {
      return cadastroCliente(cliente);
    }
    repositoryCliente.save(cliente);
    return cadastroCliente(new Cliente());

  }

}
