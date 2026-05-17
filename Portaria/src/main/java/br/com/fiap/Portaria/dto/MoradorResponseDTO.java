package br.com.fiap.Portaria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoradorResponseDTO {
    private Integer id;
    private String nome;
    private String telefone;
    private String email;
    private String bloco;
    private String apartamento;
}