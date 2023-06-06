package com.kaci.blogspring0505.service;

import java.util.List;
import java.util.Optional;

import com.kaci.blogspring0505.entities.Compte;
import com.kaci.blogspring0505.repository.ICompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaci.blogspring0505.entities.Article;
import com.kaci.blogspring0505.entities.Commentaire;
import com.kaci.blogspring0505.repository.IArticleRepository;
import com.kaci.blogspring0505.repository.ICommentaireRepository;

@Service // Service est obligatoire
//@AllArgsConstructor // All Args ou Autowired
public class RedacteurServiceImpl implements IRedacteurService {

    @Autowired
    private IArticleRepository iArticleRepository;
    @Autowired
    private ICommentaireRepository iCommentaireRepository;
    @Autowired
    private ICompteRepository iCompteRepository;

        /** Article */
    //CREATE
    @Override
    public Article creeArticle(Article article) {
        /*article.set_public(true);
        article.setModere(false);
        article.setTitre("Le titre");
        article.setContenu("Le contenu");
        article.setDate(new Date());*/
        return iArticleRepository.save(article);
    }
    //READ
    @Override
    public List<Article> listeArticle() {
        return iArticleRepository.findAll();
    }

    @Override
    public Article afficheArticle(Long idArticle) {
        return iArticleRepository.chercheArticleId(idArticle);
    }
    
    //UPDATE
    @Override
    public Article modifieArticle(Article article, Long idArticle) {
        Article articleSilExiste = iArticleRepository.chercheArticleId(idArticle); // cas erreur ?
        articleSilExiste.setTitre(article.getTitre());
        articleSilExiste.setContenu(article.getContenu());
        return iArticleRepository.save(articleSilExiste);
    }

    //DELETE

    @Override
    public String supprimeArticle(Long idArticle) {
        Article articleSilExiste = iArticleRepository.chercheArticleId(idArticle); //
        //Il faut d'abord supprimer les commentaires qui lui sont associés
        //1-suppression des commentaires
        // Méthode 1 : JPA
        //List<Commentaire> liste=iCommentaireRepository.deleteByArticle(articleSilExiste);
        // Méthode 2 : @Query
        iCommentaireRepository.supprimeCommentairesArticle(articleSilExiste);
        //2- ensuite suppression de l'article
        //Méthode 1 : JPA
        //iArticleRepository.deleteByIdArticle(idArticle);
        // Méthode 2 : @Query
        iArticleRepository.supprimeArticle(idArticle);

        return "Article supprimé !";
    }
    
    /** ------------------------*** */
    //fonctions de recherche --------
    //Article
    @Override
    public Article chercheArticle(Long id) {
        Optional<Article> article = Optional.ofNullable(iArticleRepository.chercheArticleId(id));
        if(article.isPresent()){
            return article.get();
        }
        throw new RuntimeException("Article introuvable");
    }

    /* Commentaire *******************/
    //CREATE
    // Crée un commentaire pour un article
    @Override
    public Commentaire creeCommentaire(Commentaire commentaire) {
        //commentaire.setArticle(article);
        return iCommentaireRepository.save(commentaire);
    }

    //READ
    //Affiche les commentaires d'un article
    @Override
    public List<Commentaire> commentairesArticle(Long idArticle) {
        return iCommentaireRepository.commentairesArticle(idArticle);
    }

    //Affiche un commentaire avec ID
    @Override
    public  Commentaire afficheCommentaire(Long idCommentaire){
        return iCommentaireRepository.chercheCommentaireId(idCommentaire);
    }

    //DELETE
    // Supprime un commentaire
    @Override
    public String supprimeCommentaire(Long idCommentaire) {
        iCommentaireRepository.supprimeCommentaireId(idCommentaire);
        return "Commentaire supprimé";
    }


    /***** Compte *******************/
    //CREATE
    // Dans IInternanteService

    //READ
    //cherche le compte d'un article
    @Override
    public  Compte chercheCompteArticle(Article article){
        return iCompteRepository.chercheCompteId(article.getCompte().getIdCompte());
    }

    //cherche le compte d'un article
    public Compte chercheCompteArticle(Long idArticle){ // Query
        return iCompteRepository.chercheCompteArticle(idArticle);
    }

    //cherche le compte d'un commentaire
    @Override
    public Compte chercheCompteCommentaire(Long idCommentaire) {
        //return iCompteRepository.chercheCompteId(commentaire.getCompte().getIdCompte());
        return iCompteRepository.chercheCompteCommentaire(idCommentaire);
    }

    //Recherche d'un compte par PSEUDO
    @Override
    public Compte chercheComptePseudo(String pseudo) {
        return iCompteRepository.chercheComptePseudo(pseudo);
    }
  

}
