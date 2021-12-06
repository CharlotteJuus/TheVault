// Created by carme
// Creation date 03/12/2021

package com.example.thevault.domain.mapping.dao;

import com.example.thevault.domain.model.Asset;
import com.example.thevault.domain.model.Cryptomunt;
import com.example.thevault.domain.model.Klant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Carmen Rietdijk
 * Beschrijving: De implementatie van de DAO voor Asset, waarin zowel de CRUD functionaliteit van Asset wordt geregeld,
 * alsmede het ophalen van de benodigde informatie voor het vullen van de portefeuille van de klant
 */

@Repository
public class JDBCAssetDAO implements AssetDAO{

    private final Logger logger = LoggerFactory.getLogger(JDBCAssetDAO.class);

    private JdbcTemplate jdbcTemplate;

    public JDBCAssetDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New JDBCAssetDAO");
    }

    private PreparedStatement slaAssetOpStatement(int klantId, Asset asset, Connection connection) throws SQLException {
        String sql = "INSERT INTO asset (klantId, cryptomuntId, naam, afkorting, waarde, aantal) values " +
                "(?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, klantId);
        ps.setInt(2, asset.getCryptomunt().getCryptomuntId());
        ps.setString(3, asset.getCryptomunt().getNaam());
        ps.setString(4, asset.getCryptomunt().getAfkorting());
        ps.setDouble(5, asset.getCryptomunt().getWaarde());
        ps.setDouble(6, asset.getAantal());
        return ps;
    }

    private static class AssetRowMapper implements RowMapper<Asset> {
        @Override
        public Asset mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Cryptomunt cryptomunt = new Cryptomunt(resultSet.getInt("cryptomuntId"), resultSet.getString("naam"),
                    resultSet.getString("afkorting"), resultSet.getDouble("waarde"));
            return new Asset(cryptomunt, resultSet.getDouble("aantal"));
        }
    }

    /**
     * Dit betreft het toevoegen van een cryptomunt die nog niet in de portefeuille zit
     * Dit gebeurt via een 'transactie', waarbij een klant crypto's koopt
     * @param klantId identifier van de klant die de cryptomunt koopt
     * @param asset de cryptomunt en het aantal dat de klant aanschaft
     * @return Asset de asset die de klant heeft toegevoegd
     */
    @Override
    public Asset voegNieuwAssetToeAanPortefeuille(int klantId, Asset asset) {
        /*Map<klantId, Asset> portefeuille = new HashMap<>();
        portefeuille.add(klantId, asset);
        System.out.println(portefeuille.contains(klantId));
        System.out.println(portefeuille.contains(asset))*/
        jdbcTemplate.update(connection -> slaAssetOpStatement(klantId, asset, connection));
        return asset;
    }

    //TODO Besluiten of deze methode nodig is. Eigenlijk is 'verwijderen' een speciale situatie van 'updaten' waarbij de
    // hoeveelheid verkochte cryptomunt gelijk is aan de hoeveelheid aanwezig in de portefeuille.
    /**
     * Dit betreft het verwijderen van een cryptomunt uit de portefeuille
     * Dit gebeurt via een 'transactie', waarbij een klant crypto's verkoopt
     * @param klantId identifier van de klant die de cryptomunt verkoopt
     * @param cryptomuntId de identifier van de cryptomunt die uit de portefeuille wordt verwijderd
     * @return String bericht dat de cryptomunt uit de portefeuille is verwijderd
     */

    @Override
    public String verwijderAssetUitPortefeuille(int klantId, int cryptomuntId) {
        return "Cryptomunt is verwijderd";
    }

    //TODO Besluiten of dit twee methodes moeten worden: kopen vs verkopen. Opzich gebeurt er in beide gevallen
    // hetzelfde, namelijk het aanpassen van het aantal van de cryptomunt.
    /**
     * Dit betreft het updaten van een cryptomunt die al in de portefeuille zit
     * Dit gebeurt via een 'transactie', waarbij een klant crypto's koopt of verkoopt
     * @param klantId identifier van de klant die de cryptomunt koopt/verkoopt
     * @param asset de asset waarin de klant handelt
     * @return Asset de asset na de update
     */

    @Override
    public Asset updateAsset(int klantId, Asset asset) {
        //Eerst 'geef asset' en haal daar het huidige aantal uit
        //Dan berekenen nieuwe hoeveelheid: oude hoeveelheid +/- hoeveelheid van @param asset
        //Dan opslaan cryptomunt met nieuwe hoeveelheid, en deze informatie @return
        return null;
    }

    /**
     * Dit betreft het vinden van een cryptomunt die in de portefeuille zit
     * @param klantId identifier van de klant die informatie opvraagt over de cryptomunt
     * @param cryptomuntId identifier waarover informatie wordt opgevraagd
     * @return Asset de asset (cryptomunt + aantal) waarover informatie is opgevraagd
     */

    @Override
    public Asset geefAsset(int klantId, int cryptomuntId, Connection connection) throws SQLException{
        String sql = "Select * from asset where klantId = ? AND cryptomuntId = ?;";
        return jdbcTemplate.queryForObject(sql, new Object{klantId}, new Object{cryptomuntId}, new JDBCAssetDAO.AssetRowMapper());
    }

    /**
     * Dit betreft het vinden van alle cryptomunten die in de portefeuille zitten
     * @param klantId identifier van de klant die informatie opvraagt over de cryptomunt
     * @return List</Asset> een lijst van alle Assets (cryptomunten + hoeveelheden) in het bezit van de klant
     */

    public List<Asset> geefAlleAssets(int klantId, Connection connection) throws SQLException{
        String sql = "Select * from asset where klantId = ?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, klantId);
        return jdbcTemplate.query((PreparedStatementCreator) ps, new JDBCAssetDAO.AssetRowMapper());
    }

}