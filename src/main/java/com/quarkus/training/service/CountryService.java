package com.quarkus.training.service;

import com.quarkus.training.config.MessageSource;
import com.quarkus.training.domain.Country;
import com.quarkus.training.exception.EntityNotFoundException;
import com.quarkus.training.exception.RequestException;
import com.quarkus.training.mapping.CountryMapper;
import com.quarkus.training.repository.CountryRepository;
import lombok.AllArgsConstructor;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
@AllArgsConstructor
public class CountryService {

    CountryRepository countryRepository;

    CountryMapper countryMapper;

    MessageSource messageSource;

    public List<Country> getCountries() {
        return StreamSupport.stream(countryRepository.findAll().spliterator(), false)
                .map(countryMapper::toCountry)
                .collect(Collectors.toList());
    }

    public Country getCountry(String name) {
        return countryMapper.toCountry(countryRepository.findByNameIgnoreCase(name).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("country.notfound", name))));
    }

    public Country createCountry(Country country) {
        countryRepository.findByNameIgnoreCase(country.getName())
                .ifPresent(entity -> {
                    throw new RequestException(messageSource.getMessage("country.exists", country.getName()),
                            Response.Status.CONFLICT);
                });
        return countryMapper.toCountry(countryRepository.save(countryMapper.fromCountry(country)));
    }

    public Country updateCountry(String name, Country country) {
        return countryRepository.findByNameIgnoreCase(name)
                .map(entity -> {
                    country.setName(name);
                    return countryMapper.toCountry(countryRepository.save(countryMapper.fromCountry(country)));
                }).orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("country.notfound", name)));
    }

    public void deleteCountry(String name) {
        if(countryRepository.existsById(name)) {
            try {
                countryRepository.deleteById(name);
            } catch (Exception e) {
                throw new RequestException(messageSource.getMessage("country.errordeletion", name),
                        Response.Status.CONFLICT);
            }
        }
    }

}