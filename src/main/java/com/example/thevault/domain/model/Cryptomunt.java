// Created by carme
// Creation date 30/11/2021

package com.example.thevault.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;

public class Cryptomunt {

    private int Id;
    private String name;
    private String symbol;
    private double price;
    private LocalDateTime date;

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(Cryptomunt.class);

    public Cryptomunt() {
        super();
        logger.info("New Cryptomunt, no args constructor");
    }

    /**
     * @Author Carmen
     * Constructor voor het opvragen van informatie uit de database voor Asset
     * @param cryptomuntId de identifier van de cryptomunt
     */
    public Cryptomunt(int cryptomuntId){
        this(cryptomuntId, null, null, 0, null);
    }

    public Cryptomunt (int cryptomuntId, String name, String afkorting, double price, LocalDateTime datum){
        this.Id = cryptomuntId;
        this.name = name;
        this.symbol = afkorting;
        this.price = price;
        this.date = datum;
        logger.info("Cryptomunt:" + this);
    }

    @Override
    public String toString() {
        return "Cryptomunt{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", datum=" + date +
                '}';
    }

    public int getId() {
        return Id;
    }

    public void setId(int cryptomuntId) {
        this.Id = cryptomuntId;
    }

    public String getName() {
        return name;
    }

    public void setName(String naam) {
        this.name = naam;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String afkorting) {
        this.symbol = afkorting;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double waarde) {
        this.price = waarde;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cryptomunt that = (Cryptomunt) o;
        return Id == that.Id && Double.compare(that.price, price) == 0 && name.equals(that.name) && Objects.equals(symbol, that.symbol) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, name, symbol, price, date);
    }
}
