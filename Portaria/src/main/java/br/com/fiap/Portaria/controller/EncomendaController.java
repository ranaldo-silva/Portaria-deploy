package br.com.fiap.Portaria.controller;

import br.com.fiap.Portaria.dto.EncomendaRequestDTO;
import br.com.fiap.Portaria.dto.EncomendaResponseDTO;
import br.com.fiap.Portaria.service.EncomendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/encomendas")
@Tag(name = "Encomendas", description = "Gerenciamento de encomendas recebidas na portaria")
@SecurityRequirement(name = "bearerAuth")
public class EncomendaController {

    @Autowired
    private EncomendaService encomendaService;

    @Operation(summary = "Lista todas as encomendas", description = "Retorna array com encomendas e dados do morador embutidos")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @GetMapping
    public List<EncomendaResponseDTO> listarTodas() {
        return encomendaService.listarTodas();
    }

    @Operation(summary = "Busca encomenda pelo token", description = "Usado no fluxo de retirada — porteiro digita o token e vê nome e dados do morador na tela")
    @ApiResponse(responseCode = "200", description = "Encomenda encontrada")
    @ApiResponse(responseCode = "404", description = "Token não existe")
    @GetMapping("/token/{token}")
    public ResponseEntity<EncomendaResponseDTO> buscarPorToken(@PathVariable String token) {
        try {
            return ResponseEntity.ok(encomendaService.buscarPorToken(token));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Registra nova encomenda", description = "Gera token automático (ex: A4B1Z) e seta retirada=false. O app usa o token devolvido pra disparar WhatsApp pro morador")
    @ApiResponse(responseCode = "201", description = "Encomenda criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<EncomendaResponseDTO> criar(@RequestBody EncomendaRequestDTO dto,
                                                      @AuthenticationPrincipal String email) {
        try {
            return ResponseEntity.status(201).body(encomendaService.salvar(dto, email));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Atualiza encomenda", description = "Usado se o porteiro digitou algo errado no registro")
    @ApiResponse(responseCode = "200", description = "Encomenda atualizada")
    @ApiResponse(responseCode = "404", description = "Encomenda não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<EncomendaResponseDTO> atualizar(@PathVariable Integer id,
                                                          @RequestBody EncomendaRequestDTO dto) {
        try {
            return ResponseEntity.ok(encomendaService.atualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remove encomenda", description = "Reversão de encomenda inserida erroneamente")
    @ApiResponse(responseCode = "204", description = "Encomenda removida")
    @ApiResponse(responseCode = "404", description = "Encomenda não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        try {
            encomendaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}