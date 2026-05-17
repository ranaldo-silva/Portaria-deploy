package br.com.fiap.Portaria.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FILA_ENCOMENDA = "fila.encomenda.recebida";
    public static final String FILA_RETIRADA  = "fila.retirada.realizada";

    @Bean
    public Queue filaEncomenda() {
        return new Queue(FILA_ENCOMENDA, true);
    }

    @Bean
    public Queue filaRetirada() {
        return new Queue(FILA_RETIRADA, true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}