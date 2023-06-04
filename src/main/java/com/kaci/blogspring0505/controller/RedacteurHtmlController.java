package com.kaci.blogspring0505.controller;

import java.util.Date;
import java.util.List;

import com.kaci.blogspring0505.entities.Compte;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kaci.blogspring0505.entities.Article;
import com.kaci.blogspring0505.entities.Commentaire;
import com.kaci.blogspring0505.service.IRedacteurService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // Utliser Thymleaf (jsp)
// @Primary
// @RestController // API - récupérer les données au format JSON
@AllArgsConstructor @NoArgsConstructor
public class RedacteurHtmlController {

    //@Qualifier("moderateurServiceImpl") // classe choisie en cas de conflit
    @Autowired // injection de dépendances
    private IRedacteurService iRedacteurService;
    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    /**
     * Article
     *******************/
    //CREATE
    @GetMapping("/redact/formArticle") // construit un article
    public String formArticle(Article article, Model model) {//
        model.addAttribute("article", article);

        return "articles/formArticle";
    }
    @PostMapping("/redact/saveArticle") // sauvegarde l'article
    public String saveArticle(Article article, RedirectAttributes ra) { //
        //article.set_public(true);
        //if(!article.is_public()) article.set_public(true);
        article.setModere(false);
        article.setDate(new Date());
        article.setDateModif(new Date());

        // Ajouter le Compte
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Compte compte = iRedacteurService.chercheComptePseudo(currentUserName);
        article.setCompte(compte); // inserer le compte
        //
        /*if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            System.out.println("currentUserName:::::::::::::  "+currentUserName);
            Compte compte = iRedacteurService.chercheComptePseudo(currentUserName);
            article.setCompte(compte); // inserer le compte
        }
        else {System.out.println("currentPrincipalName");}*/

        iRedacteurService.creeArticle(article);
        ra.addFlashAttribute("Article enregistré");
        return "redirect:/redact/index";
    }

    //READ
    @GetMapping("/redact/index") // liste des articles
    @PreAuthorize("hasRole('REDACT')") //
    public String listeArticle(Model model) {
        List<Article> articles = iRedacteurService.listeArticle();
        model.addAttribute("listeArticles", articles);
        String titrePage = "Nos publications";
        model.addAttribute("titrePage", titrePage);
        // Essais Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        //
        System.out.println("authentication.getPrincipal : "+authentication.getPrincipal());
       System.out.println("authentication.getAuthorities : "+authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MODER")));
        System.out.println("authentication.getCredentials : "+authentication.getCredentials());
        System.out.println("authentication.getDetails : "+authentication.getDetails());
        return "articles/listeArticles";
    }

    //Affiche article seul
    @GetMapping("/redact/affichearticle") // affiche un article selon son id
    public String afficheArticle(@RequestParam(name = "idArticle") Long id, Model model) {
        Article article = iRedacteurService.chercheArticle(id);
        model.addAttribute("article", article); // attributeName= nom attribut le doc HTML
        return "afficheArticle";
    }

    //Affiche article avec l'auteur et commentaires
    @GetMapping("/redact/listecommentairesarticle") // affiche un article avec compte et commentaires
    public String afficheArticleCommentaires(@RequestParam(name = "idArticle") Long id, Model model) {
        Article article = iRedacteurService.chercheArticle(id);
        //Compte compte = iRedacteurService.chercheCompteArticle(article);
        Compte compte = iRedacteurService.chercheCompteArticle(id);
        List<Commentaire> commentaires = iRedacteurService.commentairesArticle(id);
        model.addAttribute("article", article); // attributeName= nom attribut le doc HTML
        model.addAttribute("compte",compte);
        //model.addAttribute("pseudo",pseudo);
        model.addAttribute("listecommentaires", commentaires);
        return "articles/listeArticleCommentaires";
    }
    //UPDATE
    @GetMapping("/redact/editArticle") // édite le nouveau contenu d'un article
    public String editArticle(Model model, @RequestParam("idArticle") Long idArticle) {//(@Validated Article article, BindingResult bindingResult) {
        Article article = iRedacteurService.chercheArticle(idArticle);
        model.addAttribute("article", article);
        return "articles/editArticle";
        /*try{
            Article article = iRedacteurService.chercheArticle(idArticle);
            model.addAttribute("article",article);
            return "editArticle";
        } catch (RuntimeException e){
            return "redirect:/index";
        }*/
    }
    @PostMapping("/redact/saveModifArticle") // sauvegarde l'article
    public String saveModifArticle(Article article, RedirectAttributes ra) { //
        article.setDateModif(new Date());

        // Ajouter le Compte
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Compte compte = iRedacteurService.chercheComptePseudo(currentUserName);
        article.setCompte(compte); // inserer le compte
        //

        iRedacteurService.creeArticle(article);
        ra.addFlashAttribute("Article enregistré");
        return "redirect:/redact/index";
    }

    //DELETE
    @GetMapping("/redact/supprimearticle")
    public String supprimeArticle(@RequestParam(name = "idArticle") Long id) {
        iRedacteurService.supprimeArticle(id);
        return "redirect:/redact/index";
    }


    /** Commentaire   **********************/
    //CREATE

    // Formulaire pour créer un commentaire pour un article
    @GetMapping("/redact/formcommentaire")
    public String formCommentaire(Commentaire commentaire, @RequestParam(name = "idArticle") Long idArticle, Model model) {//
        //Article article = new Article();
        Article article = iRedacteurService.chercheArticle(idArticle);
        model.addAttribute("commentaire", commentaire);
        model.addAttribute("article", article);
        //model.addAttribute("idArticle",idArticle);
        return "commentaires/formCommentaire";
    }
    @PostMapping("/redact/saveCommentaire") // sauvegarde le commentaire
    public String saveCommentaire(Commentaire commentaire, @RequestParam(name = "idArticle") Long id) { //
        //commentaire.set_public(true);
        Article article = iRedacteurService.chercheArticle(id);
        commentaire.setArticle(article);
        commentaire.setModere(false);
        commentaire.setDate(new Date());

        // Ajouter le Compte au commentaire
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Compte compte = iRedacteurService.chercheComptePseudo(currentUserName);
        commentaire.setCompte(compte); // inserer le compte
        //
        iRedacteurService.creeCommentaire(commentaire);
        return "redirect:/redact/index";
        //return "redirect:/redact/listecommentairesarticle(idArticle=id)"; //?
    }

    //READ
    //Affiche un commentaire
    @GetMapping("/redact/affichecommentaire") // avec son id
    public String afficheCommentaire(@RequestParam(name = "idCommentaire") Long id, Model model) {
        Commentaire commentaire = iRedacteurService.afficheCommentaire(id);
        model.addAttribute("commentaire", commentaire);
        //Compte compte = iRedacteurService.chercheCompteCommentaire(commentaire);
        Compte compte = iRedacteurService.chercheCompteCommentaire(id);
        //String pseudo
        model.addAttribute("compte",compte);
       // model.addAttribute("listecommentaires", commentaires);
        return "commentaires/afficheCommentaire";
    }

    //UPDATE
    @GetMapping("/redact/editcommentaire")
    public String editCommentaire(Commentaire commentaire, @RequestParam(name = "idCommentaire") Long idCommentaire, @RequestParam(name = "idArticle") Long idArticle, Model model) {//
        //Article article = new Article();
        Article article = iRedacteurService.chercheArticle(idArticle);
        Commentaire commentaire1 = iRedacteurService.afficheCommentaire(idCommentaire);
        model.addAttribute("commentaire", commentaire1);
        model.addAttribute("article", article);
        //model.addAttribute("idArticle",idArticle);
        return "commentaires/editCommentaire";
    }

    //DELETE
    //supprime un commentaire

}
