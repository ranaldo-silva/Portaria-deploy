package br.com.fiap.Portaria.entity;

import br.com.fiap.Portaria.dto.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TPL_USUARIO")
public class Usuario {

    @Id
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "FIREBASE_UID", unique = true)
    private String firebaseUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "PERFIL", nullable = false)
    private Role perfil;

    @Column(name = "ID_MORADOR")
    private Integer idMorador;

    @Column(name = "ID_PORTARIA")
    private Integer idPortaria;
}