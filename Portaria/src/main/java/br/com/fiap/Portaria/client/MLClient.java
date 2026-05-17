package br.com.fiap.Portaria.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

// URL vem da variável de ambiente URL_ML (configurada no Railway/Render)
@FeignClient(name = "ml-client", url = "${URL_ML:http://localhost:8001}")
public interface MLClient {

    @PostMapping("/predict")
    Map<String, Object> predict(@RequestBody Map<String, Object> dados);
}
