package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.Estado;
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
public class ControlEstado {

  @Autowired
  private RepositoryEstado repositoryEstado;

  @GetMapping("/cadastroEstado")
  public ModelAndView cadastroEstado(Estado estado) {

    ModelAndView mv = new ModelAndView("rota");
    mv.addObject("estado", estado);
    return mv;
  }

  @GetMapping("/editarEstado/{id}")
  public ModelAndView editarEstado(@PathVariable Integer id) {
    Optional<Estado> estado = repositoryEstado.findById(id);
    return cadastroEstado(estado.get());
  }

  @GetMapping("/deletarEstado/{id}")
  public ModelAndView deleteEstado(@PathVariable Integer id) {
    Optional<Estado> estado = repositoryEstado.findById(id);
    repositoryEstado.deleteById(id);
    return listarEstado();
  }

  @GetMapping("/listarEstado")
  public ModelAndView listarEstado() {
    ModelAndView mv = new ModelAndView("/listarEstado");
    mv.addObject("estados", repositoryEstado.findAll());
    return mv;
  }

  @PostMapping("/salvarEstado")
  public ModelAndView salvarEstado(Estado estado, BindingResult result) {
    if (result.hasErrors()) {
      return cadastroEstado(estado);
    }
    repositoryEstado.save(estado);
    return cadastroEstado(new Estado());

  }

}
