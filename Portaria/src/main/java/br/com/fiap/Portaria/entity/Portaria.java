package br.com.fiap.Portaria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TPL_PORTARIA")
public class Portaria {

    @Id
    private Integer idPortaria;

    private String nomePorteiro;
    private String turno;
    private String contato;
    private Date dataRegistro;

    @OneToMany(mappedBy = "portaria")
    private List<Retirada> retiradas;

}