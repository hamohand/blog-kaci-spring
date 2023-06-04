package com.kaci.blogspring0505.service;

import com.kaci.blogspring0505.entities.Compte;

import java.util.List;

public interface IAdminService {
    /* Compte */

    //READ
    // Liste des comptes valides
    public List<Compte> listeComptesNonValides();

}
