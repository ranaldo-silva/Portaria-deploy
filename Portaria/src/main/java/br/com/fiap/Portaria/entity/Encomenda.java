package br.com.fiap.Portaria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TPL_ENCOMENDA")
public class Encomenda {

    @Id
    private Integer idEncomenda;

    private String descricao;
    private Date dataRecebida;
    private String status;

    @ManyToOne
    @JoinColumn(name = "ID_MORADOR")
    private Morador morador;

    @OneToOne
    @JoinColumn(name = "ID_RETIRADA")
    private Retirada retirada;

    @Column(name = "TOKEN_ENCOMENDA", unique = true)
    private String tokenEncomenda;

    private String origem;

    @Column(name = "RETIRADA")
    private Boolean foiRetirada = false;

    @Column(name = "RETIRADA_EM")
    private LocalDateTime retiradaEm;

}