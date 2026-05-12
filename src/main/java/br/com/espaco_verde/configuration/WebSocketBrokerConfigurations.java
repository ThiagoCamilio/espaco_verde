package br.com.espaco_verde.configuration;

import br.com.espaco_verde.service.ServiceToken;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSocketBrokerConfigurations implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private ServiceToken serviceToken;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:4200")
                .withSockJS();

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // 2. Só fazemos algo se o accessor existir
                if (accessor != null) {
                    StompCommand command = accessor.getCommand();

                    // 3. Verificamos se é o aperto de mão inicial do cliente
                    if (StompCommand.CONNECT.equals(command) || StompCommand.STOMP.equals(command)) {

                        // Pega o token enviado pelo Angular
                        String authHeader = accessor.getFirstNativeHeader("Authorization");

                        if (authHeader != null && authHeader.startsWith("Bearer ")) {
                            String token = authHeader.substring(7);
                            String userId = serviceToken.extractUserId(token);

                            if (userId != null) {
                                // Cria o crachá
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(userId, null, null);

                                // 4. A MÁGICA: Ao usar o setUser aqui, o Spring automaticamente vincula
                                // esse usuário a todos os eventos futuros dessa sessão!
                                accessor.setUser(authentication);

                                System.out.println("✅ SUCESSO: Usuário ID " + userId + " vinculado à sessão " + accessor.getSessionId());
                            } else {
                                System.out.println("❌ ERRO: ID extraído do token é nulo.");
                            }
                        } else {
                            System.out.println("⚠️ AVISO: Nenhum Auth-Token encontrado nos cabeçalhos.");
                        }
                    }
                }

                // Retorna a mensagem original! (Não use MessageBuilder, pois ele desvincula os atributos)
                return message;
            }
        });
    }
}
