package com.kaci.blogspring0505.controller;

import com.kaci.blogspring0505.entities.Compte;
import com.kaci.blogspring0505.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminHtmlController {
    @Autowired
    private IAdminService iAdminService;

    /*** Compte ****************/
    //READ
    //Accueil modérateur
    @GetMapping("/admin/index")
    public String indexAdmin(){
        return "indexAdmin";
    }

    // Liste des comptes non validés
    @GetMapping("/admin/listecomptesnonvalide") // index des articles
    public String listeCompteNonValide(Model model) {
        List<Compte> comptes = iAdminService.listeComptesNonValides();
        model.addAttribute("liste", comptes);
        return "comptes/listeComptesNonValide";
    }



}
