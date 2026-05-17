package br.com.fiap.Portaria.service;

import br.com.fiap.Portaria.config.RabbitMQConfig;
import br.com.fiap.Portaria.repository.EncomendaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetiradaConsumer {

    @Autowired
    private EncomendaRepository encomendaRepository;

    @RabbitListener(queues = RabbitMQConfig.FILA_RETIRADA)
    public void processarRetirada(String mensagem) {
        System.out.println("Retirada processada na fila: " + mensagem);

        // extrai o ID da encomenda da mensagem e atualiza status
        if (mensagem.contains("EncomendaID:")) {
            String[] partes = mensagem.split("EncomendaID:");
            Integer encomendaId = Integer.parseInt(partes[1].trim());

            encomendaRepository.findById(encomendaId).ifPresent(encomenda -> {
                encomenda.setStatus("RETIRADA");
                encomendaRepository.save(encomenda);
                System.out.println("Status da encomenda " + encomendaId + " atualizado para RETIRADA");
            });
        }
    }
}