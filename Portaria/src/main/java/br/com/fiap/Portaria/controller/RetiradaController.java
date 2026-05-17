package br.com.fiap.Portaria.controller;

import br.com.fiap.Portaria.dto.RetiradaRequestDTO;
import br.com.fiap.Portaria.dto.RetiradaResponseDTO;
import br.com.fiap.Portaria.service.RetiradaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retiradas")
@Tag(name = "Retiradas", description = "Fluxo de retirada de encomendas pelo token")
@SecurityRequirement(name = "bearerAuth")
public class RetiradaController {

    @Autowired
    private RetiradaService retiradaService;

    @Operation(summary = "Registra retirada de encomenda", description = "Porteiro informa o token falado pelo morador. A API localiza a encomenda, marca retirada=true e registra o timestamp")
    @ApiResponse(responseCode = "201", description = "Retirada registrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Token inválido ou encomenda já retirada")
    @PostMapping
    public ResponseEntity<RetiradaResponseDTO> registrarRetirada(@RequestBody RetiradaRequestDTO dto) {
        try {
            return ResponseEntity.status(201).body(retiradaService.registrarRetirada(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}