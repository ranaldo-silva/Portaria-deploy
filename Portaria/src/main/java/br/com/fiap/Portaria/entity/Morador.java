package br.com.fiap.Portaria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TPL_MORADOR")
public class Morador {

    @Id
    @Column(name = "ID_MORADOR")
    private Integer idMorador;

    private String nome;
    private String email;
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "ID_APARTAMENTO")
    private Apartamento apartamento;

    @OneToMany(mappedBy = "morador")
    private List<Encomenda> encomendas = new ArrayList<>();

    @OneToMany(mappedBy = "morador")
    private List<Retirada> retiradas = new ArrayList<>();

}