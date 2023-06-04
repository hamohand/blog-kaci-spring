package com.kaci.blogspring0505.controller;

import java.util.Date;
import java.util.List;

import com.kaci.blogspring0505.entities.Commentaire;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kaci.blogspring0505.entities.Article;
import com.kaci.blogspring0505.service.IRedacteurService;


//@Controller // Utliser Thymleaf (jsp)
//@RestController // API - récupérer les données en format JSON

public class RedacteurRestController {

    @Autowired // injection de dépendance
    private IRedacteurService iRedacteurService;

    //CREATE

    @PostMapping("/redact/nouvelarticle")
    public Article creeArticle(@RequestBody Article article){ //@RequestBody
        article.set_public(true); // par défaut (consignes)
        article.setModere(false); // par défaut (consignes)
        article.setDate(new Date());
        return iRedacteurService.creeArticle(article);
    }

    //READ

    @GetMapping("/redact/index") // index des articles
    public List<Article> listeArticle() {
        return iRedacteurService.listeArticle();
    }

    @GetMapping("/redact/article/{idArticle}") // afficher un article
    public Article afficheArticle(@PathVariable Long idArticle) { //@PathVariable
        return iRedacteurService.afficheArticle(idArticle);
    }

    //UPDATE
    @PutMapping("/redact/modifie/{idArticle}")
    public Article modifieArticle(@RequestBody Article article, @PathVariable Long idArticle){
        return iRedacteurService.modifieArticle(article, idArticle);
    }

    //DELETE
    @DeleteMapping("/redact/supprime/{idArticle}")
    public String supprimeArticle(@PathVariable Long idArticle){
        return iRedacteurService.supprimeArticle(idArticle);
    }

    /* Commentaire **************************/
//CREATE
    // Crée un commentaire d'un article
    @PostMapping("/redact/commentaire/{article}")
    public Commentaire creeCommentaireArticle(@RequestBody Commentaire commentaire, @PathVariable Article article) {
        commentaire.set_public(true); // par défaut (consignes)
        commentaire.setModere(false); // par défaut (consignes)
        commentaire.setDate(new Date());
        commentaire.setArticle(article);
        return iRedacteurService.creeCommentaire(commentaire);
    }

    //READ
    //Affiche les commentaires d'un article
    @GetMapping("/redact/commentaires/{article}")
    public List<Commentaire> listeCommentairesParArticle(@PathVariable Article article) {
        return iRedacteurService.commentairesArticle(article.getIdArticle());
    }
}
