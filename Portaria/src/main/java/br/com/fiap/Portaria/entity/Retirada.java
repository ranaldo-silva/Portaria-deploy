package br.com.fiap.Portaria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TPL_RETIRADA")
public class Retirada {

    @Id
    private Integer idRetirada;

    private Date dataRetirada;
    private String tokenRetirada;

    @ManyToOne
    @JoinColumn(name = "ID_MORADOR")
    private Morador morador;

    @ManyToOne
    @JoinColumn(name = "ID_PORTARIA")
    private Portaria portaria;

}
