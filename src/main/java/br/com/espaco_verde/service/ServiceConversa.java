package br.com.espaco_verde.service;

import br.com.espaco_verde.entity.Conversa;
import br.com.espaco_verde.entity.MensagemCliente;
import br.com.espaco_verde.entity.Pagina;
import br.com.espaco_verde.entity.Produto;
import br.com.espaco_verde.repository.RepositoryConversa;
import br.com.espaco_verde.repository.RepositoryPagina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceConversa {

    @Autowired
    private RepositoryConversa repositoryConversa;

    @Autowired
    private ServiceMensagens serviceMensagens;

    @Autowired
    private ServiceProduto serviceProduto;

    @Autowired
    private RepositoryPagina repositoryPagina;

    public void processarMensagem(MensagemCliente mensagem) throws Exception {

        Conversa conversa = getConversa(mensagem);
        String retorno;
        Pagina paginaAtual = conversa.getPaginaAtual();
        String respostaCliente = mensagem.getTexto().toLowerCase();

        for (Pagina p : paginaAtual.getProximasPaginas()){

            if(p.getId().equals(respostaCliente)){
                paginaAtual = p;
                break;
            }
        }

        if (paginaAtual.equals(conversa.getPaginaAtual())){

            retorno = "Desculpe, não entendi, digite novamente";
            serviceMensagens.sendTextMessage(mensagem, retorno);

        }

        switch (paginaAtual.getId()){
            case "menu_principal":

                serviceMensagens.sendButtonMessage(mensagem, paginaAtual);
                break;

            case "produtos":

                List<Produto> produtos = serviceProduto.listAll();
                serviceMensagens.sendCarouselMessage(mensagem, produtos);
                break;

            case "carrinho":
                retorno = "Ainda não disponivel";
                serviceMensagens.sendTextMessage(mensagem, retorno);
                break;

            case "checkout":
                retorno = "Ainda não disponivel";
                serviceMensagens.sendTextMessage(mensagem, retorno);
                break;

            default:
                //conversa.setEstadoConversa(EstadoConversa.MENU_PRINCIPAL);
                //serviceMensagens.sendButtonMessage(mensagem);
                break;
        }

        conversa.setPaginaAtual(paginaAtual);
        repositoryConversa.save(conversa);



    }

    public Conversa getConversa(MensagemCliente mensagem){

        if (repositoryConversa.existsByTelefone(mensagem.getRemetente())){

            return repositoryConversa.findByTelefone(mensagem.getRemetente());

        }else {

            Conversa conversa = new Conversa(mensagem.getRemetente(), mensagem.getId(), repositoryPagina.findById("menu_principal"));
            repositoryConversa.save(conversa);
            return conversa;

        }

    }

}
