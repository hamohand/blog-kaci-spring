package com.kaci.blogspring0505.service;

import com.kaci.blogspring0505.entities.Compte;
import com.kaci.blogspring0505.repository.ICompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements IAdminService{
   /* public AdminServiceImpl(IArticleRepository iArticleRepository, ICommentaireRepository iCommentaireRepository, ICompteRepository iCompteRepository) {
        super(iArticleRepository, iCommentaireRepository, iCompteRepository);
    }*/
    @Autowired
    private ICompteRepository iCompteRepository;
    /**
     * Compte
     *******************/
    //READ
    @Override
    public List<Compte> listeComptesNonValides() {
        return iCompteRepository.listeComptesNonValides();
    }

}
