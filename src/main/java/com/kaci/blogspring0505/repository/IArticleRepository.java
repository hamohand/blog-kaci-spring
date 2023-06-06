package com.kaci.blogspring0505.repository;

import com.kaci.blogspring0505.entities.Commentaire;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.kaci.blogspring0505.entities.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IArticleRepository extends JpaRepository<Article, Long>{
    /*** Articles *************/
    //READ
    // Recherche d'un article avec son Id
        //Article findByIdArticle(Long idArticle);
    @Query("SELECT a FROM Article a WHERE a.idArticle= :x")
    Article chercheArticleId(@Param("x") Long idArticle);

    // Liste des articles en attente de modération
        //List<Article> findArticlesByModereIsFalse();
    @Query("SELECT a FROM Article a WHERE a.modere=false")
    List<Article> articlesNonModeres();

    // Accueil site : Liste Articles publiques
        //List<Article> findArticlesBy_publicIsTrue();
    @Query("SELECT a FROM Article a WHERE a._public=true")
    List<Article> articlesPublics();

    //DELETE 
    // supprimer un article (supprimer d'abord ses commentaires, voir ICommentaireRepository)
    //Méthode 1 : JPA avec l'annotation @Transactional
    @Transactional
    void deleteByIdArticle(Long idArticle);
    //Méthode 2 : @Query avec les 2 annotations @Transactional et @Modifying
    @Transactional
    @Modifying
    @Query("DELETE FROM Article a WHERE  a.idArticle= :x")
    void supprimeArticle(@Param("x") Long idArticle);

}

