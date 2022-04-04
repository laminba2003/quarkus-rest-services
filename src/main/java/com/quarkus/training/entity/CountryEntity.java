package com.quarkus.training.entity;

import com.quarkus.training.domain.Country;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "countries")
public class CountryEntity {

    @Id
    private String name;
    private String capital;
    private int population;

    public Country toCountry() {
        return new Country(name, capital, population);
    }

    public static CountryEntity fromCountry(Country country) {
        CountryEntity entity = new CountryEntity();
        entity.setName(country.getName());
        entity.setCapital(country.getCapital());
        entity.setPopulation(country.getPopulation());
        return entity;
    }

}