package br.com.espaco_verde.service;

import br.com.espaco_verde.entity.Conversa;
import br.com.espaco_verde.entity.EstadoConversa;
import br.com.espaco_verde.entity.MensagemCliente;
import br.com.espaco_verde.repository.RepositoryConversa;
import br.com.espaco_verde.repository.RepositoryTemplateResposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceConversa {

    @Autowired
    private RepositoryConversa repositoryConversa;

    @Autowired
    private ServiceMensagens serviceMensagens;

    @Autowired
    private RepositoryTemplateResposta repositoryTemplateResposta;

    public void processarMensagem(MensagemCliente mensagem){

        Conversa conversa = getConversa(mensagem);
        String resposta;
        EstadoConversa estadoAtual = conversa.getEstadoConversa();
        String texto = mensagem.getTexto().toLowerCase();

        switch (estadoAtual){
            case MENU_PRINCIPAL:
                System.out.println(estadoAtual);
                System.out.println(texto);
                if (texto.equals("produtos")){
                    resposta = repositoryTemplateResposta.findById("produtos").getTexto();
                    conversa.setEstadoConversa(EstadoConversa.ESCOLHA_PRODUTOS);
                    serviceMensagens.sendTextMessage(mensagem, resposta);
                    break;

                }else {
                    resposta = "Desculpe, não entendi, digite novamente";
                    serviceMensagens.sendTextMessage(mensagem, resposta);
                    serviceMensagens.sendButtonMessage(mensagem);
                    break;
                }

            case ESCOLHA_PRODUTOS:
                resposta = "Ainda não disponivel";
                serviceMensagens.sendTextMessage(mensagem, resposta);
                break;

            case CARRINHO:
                resposta = "Ainda não disponivel";
                serviceMensagens.sendTextMessage(mensagem, resposta);
                break;

            case FINALIZANDO_COMPRA:
                resposta = "Ainda não disponivel";
                serviceMensagens.sendTextMessage(mensagem, resposta);
                break;

            default:
                conversa.setEstadoConversa(EstadoConversa.MENU_PRINCIPAL);
                serviceMensagens.sendButtonMessage(mensagem);
                break;
        }

        repositoryConversa.save(conversa);



    }

    public Conversa getConversa(MensagemCliente mensagem){

        if (repositoryConversa.existsByTelefone(mensagem.getRemetente())){

            return repositoryConversa.findByTelefone(mensagem.getRemetente());

        }else {

            Conversa conversa = new Conversa(mensagem.getRemetente(), mensagem.getId(), EstadoConversa.NOVA);
            repositoryConversa.save(conversa);
            return conversa;

        }

    }

}
