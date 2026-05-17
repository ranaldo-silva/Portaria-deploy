package br.com.fiap.Portaria.client;

import br.com.fiap.Portaria.dto.EncomendaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Aponta para si mesmo — usado internamente
@FeignClient(name = "encomenda-client", url = "http://localhost:${PORT:8080}")
public interface EncomendaClient {

    @GetMapping("/encomendas/{id}")
    EncomendaResponseDTO buscarPorId(@PathVariable("id") Integer id);
}
