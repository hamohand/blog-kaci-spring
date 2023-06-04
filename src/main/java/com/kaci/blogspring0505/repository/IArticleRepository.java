package com.kaci.blogspring0505.repository;

import com.kaci.blogspring0505.entities.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;

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
}

