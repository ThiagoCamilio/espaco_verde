package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.Cidade;
import br.com.espaco_verde.entity.Colaborador;
import br.com.espaco_verde.repository.RepositoryCidade;
import br.com.espaco_verde.repository.RepositoryColaborador;
import br.com.espaco_verde.repository.RepositoryEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ControlColaborador {

  @Autowired
  private RepositoryCidade repositoryCidade;

  @Autowired
  private RepositoryColaborador repositoryColaborador;

  @GetMapping("/cadastroColaborador")
  public ModelAndView cadastroColaborador(Colaborador colaborador) {

    ModelAndView mv = new ModelAndView("rota");
    mv.addObject("colaborador", colaborador);
    mv.addObject("listaCidades", repositoryCidade.findAll());
    return mv;
  }

  @GetMapping("/editarColaborador/{id}")
  public ModelAndView editarColaborador(@PathVariable Integer id) {
    Optional<Colaborador> colaborador = repositoryColaborador.findById(id);
    return cadastroColaborador(colaborador.get());
  }

  @GetMapping("/deletarColaborador/{id}")
  public ModelAndView deletColaborador(@PathVariable Integer id) {
    Optional<Colaborador> colaborador = repositoryColaborador.findById(id);
    repositoryColaborador.deleteById(id);
    return listarColaborador();
  }

  @GetMapping("/listarColaborador")
  public ModelAndView listarColaborador() {
    ModelAndView mv = new ModelAndView("/listarColaborador");
    mv.addObject("colaboradores", repositoryColaborador.findAll());
    return mv;
  }

  @PostMapping("/salvarColaborador")
  public ModelAndView salvarColaborador(Colaborador colaborador, BindingResult result) {
    if (result.hasErrors()) {
      return cadastroColaborador(colaborador);
    }
    repositoryColaborador.save(colaborador);
    return cadastroColaborador(new Colaborador());

  }

}
