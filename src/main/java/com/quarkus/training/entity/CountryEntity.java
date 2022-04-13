package com.quarkus.training.entity;

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
}