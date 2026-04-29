package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.Cidade;
import br.com.espaco_verde.entity.Estado;
import br.com.espaco_verde.repository.RepositoryCidade;
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
public class ControlCidade {

  @Autowired
  private RepositoryEstado repositoryEstado;

  @Autowired
  private RepositoryCidade repositoryCidade;

  @GetMapping("/cadastroCidade")
  public ModelAndView cadastroCidade(Cidade cidade) {

    ModelAndView mv = new ModelAndView("rota");
    mv.addObject("cidade", cidade);
    mv.addObject("listaEstados", repositoryEstado.findAll());
    return mv;
  }

  @GetMapping("/editarCidade/{id}")
  public ModelAndView editarCidade(@PathVariable Integer id) {
    Optional<Cidade> cidade = repositoryCidade.findById(id);
    return cadastroCidade(cidade.get());
  }

  @GetMapping("/deletarCidade/{id}")
  public ModelAndView deletCidade(@PathVariable Integer id) {
    Optional<Cidade> cidade = repositoryCidade.findById(id);
    repositoryCidade.deleteById(id);
    return listarCidade();
  }

  @GetMapping("/listarCidade")
  public ModelAndView listarCidade() {
    ModelAndView mv = new ModelAndView("/listarCidade");
    mv.addObject("cidades", repositoryCidade.findAll());
    return mv;
  }

  @PostMapping("/salvarCidade")
  public ModelAndView salvarCidade(Cidade cidade, BindingResult result) {
    if (result.hasErrors()) {
      return cadastroCidade(cidade);
    }
    repositoryCidade.save(cidade);
    return cadastroCidade(new Cidade());

  }

}
