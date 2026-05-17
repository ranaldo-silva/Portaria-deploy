package br.com.fiap.Portaria.controller;

import br.com.fiap.Portaria.dto.MoradorRequestDTO;
import br.com.fiap.Portaria.dto.MoradorResponseDTO;
import br.com.fiap.Portaria.service.MoradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moradores")
@Tag(name = "Moradores", description = "Gerenciamento de moradores do condomínio")
@SecurityRequirement(name = "bearerAuth")
public class MoradorController {

    @Autowired
    private MoradorService moradorService;

    @Operation(summary = "Lista todos os moradores", description = "Retorna array com moradores e seus apartamentos")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @GetMapping
    public List<MoradorResponseDTO> listarTodos() {
        return moradorService.listarTodos();
    }

    @Operation(summary = "Cadastra novo morador", description = "Somente ADMIN pode cadastrar. Role padrão é MORADOR")
    @ApiResponse(responseCode = "201", description = "Morador criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<MoradorResponseDTO> criar(@RequestBody MoradorRequestDTO dto) {
        try {
            return ResponseEntity.status(201).body(moradorService.salvar(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Atualiza morador", description = "Somente ADMIN pode atualizar")
    @ApiResponse(responseCode = "200", description = "Morador atualizado")
    @ApiResponse(responseCode = "404", description = "Morador não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<MoradorResponseDTO> atualizar(@PathVariable Integer id,
                                                        @RequestBody MoradorRequestDTO dto) {
        try {
            return ResponseEntity.ok(moradorService.atualizar(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remove morador", description = "Somente ADMIN pode remover")
    @ApiResponse(responseCode = "204", description = "Morador removido")
    @ApiResponse(responseCode = "404", description = "Morador não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        try {
            moradorService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}