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
@Table(name = "TPL_APARTAMENTO")
public class Apartamento {

    @Id
    private Integer idApartamento;

    private Integer torre;
    private String bloco;
    private String numero;

    @OneToMany(mappedBy = "apartamento")
    private List<Morador> moradores;

}
