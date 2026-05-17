package br.com.fiap.Portaria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class RetiradaResponseDTO {
    private Integer idRetirada;
    private Date dataRetirada;
    private String tokenRetirada;
    private Integer moradorId;
    private Integer portariaId;

}