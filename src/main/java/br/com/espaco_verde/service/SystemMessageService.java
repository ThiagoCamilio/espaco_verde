package br.com.espaco_verde.service;

import br.com.espaco_verde.entity.SystemMessage;
import br.com.espaco_verde.repository.SystemMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SystemMessageService {

    @Autowired
    private SystemMessageRepository systemMessageRepository;


    public String getMessage(String messageKey, Map<String, String> variables){
        SystemMessage systemMessage = systemMessageRepository.findByMessageKey(messageKey).
                orElseThrow(() -> new RuntimeException("Mensagem não configurada " +messageKey));

        String template = systemMessage.getContent();

        if(variables == null || variables.isEmpty()){
            return template;
        }

        for(Map.Entry<String, String> entry : variables.entrySet()){
            String placeholder = "{{" + entry.getKey() + "}}";
            template = template.replace(placeholder, entry.getValue());
        }

        return template;
    }
}
