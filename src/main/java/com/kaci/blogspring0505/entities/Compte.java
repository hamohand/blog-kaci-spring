package com.kaci.blogspring0505.entities;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Compte implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idCompte;
    private String email;
    @Column(unique = true, nullable = false)
    private String pseudo;
    private String motDePasse;
    private boolean efface;
    private boolean bani;
    private boolean valide;
    private boolean supressionDonnee;
    private boolean signale;
    @ManyToOne
    private TypeCompte typeCompte;

}
