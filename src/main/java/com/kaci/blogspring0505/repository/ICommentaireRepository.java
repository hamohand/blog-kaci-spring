package com.kaci.blogspring0505.repository;

import java.util.List;

import com.kaci.blogspring0505.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kaci.blogspring0505.entities.Article;
import com.kaci.blogspring0505.entities.Commentaire;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ICommentaireRepository extends JpaRepository<Commentaire, Long> {
    //CREATE
    // Crée un commentaire d'un article -> définie dans service

//READ
    //liste des commentaires d'un article
        //List<Commentaire> findCommentairesByArticle(Article article); //OK
    @Query("SELECT a FROM Commentaire a INNER JOIN Article b ON a.article.idArticle=b.idArticle WHERE b.idArticle= :x")
    List<Commentaire> commentairesArticle(@Param("x") Long idArticle);

    // Affiche UN commentaire
        //Commentaire findCommentaireByIdCommentaire(Long idCommentaire);
    @Query("SELECT a FROM Commentaire a WHERE a.idCommentaire= :x")
    Commentaire chercheCommentaireId(@Param("x") Long idCommentaire);

    //Liste Commentaires publiques d'un article
        //List<Commentaire> findCommentairesBy_publicIsTrueAndArticle(Article article);
    @Query("SELECT a FROM Commentaire a INNER JOIN Article b ON a.article.idArticle=b.idArticle WHERE a._public=true  AND b.idArticle= :x")
    List<Commentaire> commentairesPublicsArticle(@Param("x") Long idArticle);

    //liste des commentaires non encore modérés
        //List<Commentaire> findCommentairesByModereIsFalse();
    @Query("SELECT a FROM Commentaire a WHERE a.modere=false")
    List<Commentaire> commentairesNonmoderes();
}
