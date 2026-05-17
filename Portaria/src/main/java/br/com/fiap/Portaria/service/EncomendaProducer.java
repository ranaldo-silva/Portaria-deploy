package br.com.fiap.Portaria.service;

import br.com.fiap.Portaria.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncomendaProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void notificarEncomendaRecebida(String mensagem) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_ENCOMENDA, mensagem);
        System.out.println("Mensagem enviada para fila encomenda: " + mensagem);
    }
}
