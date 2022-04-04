package com.quarkus.training.service;

import com.quarkus.training.domain.Country;
import com.quarkus.training.entity.CountryEntity;
import com.quarkus.training.repository.CountryRepository;
import lombok.AllArgsConstructor;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
@AllArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public List<Country> getCountries() {
        return StreamSupport.stream(countryRepository.findAll().spliterator(), false)
                .map(CountryEntity::toCountry)
                .collect(Collectors.toList());
    }
}
