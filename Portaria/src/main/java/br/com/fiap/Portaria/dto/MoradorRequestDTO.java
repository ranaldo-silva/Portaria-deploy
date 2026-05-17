package br.com.fiap.Portaria.dto;

import lombok.Data;

@Data
public class MoradorRequestDTO {
    private String nome;
    private String telefone;
    private String email;
    private Integer apartamentoId;
}