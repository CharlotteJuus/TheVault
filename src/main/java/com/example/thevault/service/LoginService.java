// Created by Elise Olthof
// Creation date 12-12-2021

package com.example.thevault.service;

import com.example.thevault.domain.mapping.repository.RootRepository;
import com.example.thevault.domain.model.Klant;
import com.example.thevault.domain.model.Rekening;
import com.example.thevault.domain.transfer.LoginDto;
import com.example.thevault.support.authorization.AuthorizationService;
import com.example.thevault.support.hashing.BCryptWachtwoordHash;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoginService {

    private RootRepository rootRepository;
    private AuthorizationService authorizationService;
    private RekeningService rekeningService;

    private UUID opaakToken;
    private String jwtToken;

    private final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    public LoginService(RootRepository rootRepository, AuthorizationService authorizationService, RekeningService rekeningService) {
        super();
        this.rootRepository = rootRepository;
        this.authorizationService = authorizationService;
        this.rekeningService = rekeningService;
        logger.info("New LoginService......");
    }

    public String koppelTokenNaValidatieLogin (Klant klant){
        authorizationService.authoriseerKlantMetOpaakToken(klant);

        // nog goed toevoegen:
        return jwtToken;
    }

    /**
     * Wim 20211207
     * @return Klant als combinatie gebruikersnaam en wachtwoord correct is, anders geef foutmelding
     */

    public Klant valideerLogin (LoginDto loginDto){
        //vraag wachtwoord op via RootRepos
        if(vindKlantByGebruikersnaam(loginDto.getGebruikersnaam()) == null){
           return null;
        }
        String encryptedWachtwoord = vindKlantByGebruikersnaam(loginDto.getGebruikersnaam()).getWachtwoord();
        String wachtwoord = new String(Base64.decodeBase64(encryptedWachtwoord));

        if(!BCryptWachtwoordHash.verifyHash(loginDto.getWachtwoord(), wachtwoord)){
            return null;
        }
        return vindKlantByGebruikersnaam(loginDto.getGebruikersnaam());
    }

    public Klant vindKlantByGebruikersnaam(String username){
        Klant klant = rootRepository.vindKlantByGebruikersnaam(username);
        //tijdelijke oplossing
        if(klant != null){
        Rekening rekening = rekeningService.creeerRekening(klant);
        klant.setRekening(rekening);}
        return klant;
    }


}