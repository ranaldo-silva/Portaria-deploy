package br.com.fiap.Portaria.service;

import br.com.fiap.Portaria.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetiradaProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void notificarRetiradaRealizada(String mensagem) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_RETIRADA, mensagem);
        System.out.println("Mensagem enviada para fila retirada: " + mensagem);
    }
}