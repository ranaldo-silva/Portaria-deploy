package br.com.fiap.Portaria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncomendaResponseDTO {

    private Integer id;
    private String tokenEncomenda;
    private String origem;
    private String descricao;
    private Boolean foiRetirada;
    private java.util.Date dataRecebida;
    private java.time.LocalDateTime retiradaEm;
    private MoradorResumoDTO morador;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoradorResumoDTO {
        private Integer id;
        private String nome;
    }
}
