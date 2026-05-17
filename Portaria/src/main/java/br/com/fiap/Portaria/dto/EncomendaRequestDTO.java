package br.com.fiap.Portaria.dto;

import lombok.Data;

@Data
public class EncomendaRequestDTO {
    private Integer moradorId;
    private String origem;
    private String descricao;

}