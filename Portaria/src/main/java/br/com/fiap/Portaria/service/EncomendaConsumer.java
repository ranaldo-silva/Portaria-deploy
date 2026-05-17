package br.com.fiap.Portaria.service;

import br.com.fiap.Portaria.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EncomendaConsumer {

    @RabbitListener(queues = RabbitMQConfig.FILA_ENCOMENDA)
    public void processarEncomenda(String mensagem) {
        System.out.println("Encomenda recebida na fila: " + mensagem);
        // aqui entraria lógica de notificação, email, etc.
    }
}