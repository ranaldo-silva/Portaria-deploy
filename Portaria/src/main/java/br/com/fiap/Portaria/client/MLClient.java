package br.com.fiap.Portaria.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "ml-client", url = "${ML_SERVICE_URL:http://localhost:8001}")
public interface MLClient {

    @PostMapping("/predict")
    Map<String, Object> predict(@RequestBody Map<String, Object> dados);
}
