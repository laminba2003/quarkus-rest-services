package com.quarkus.training.service;

import com.quarkus.training.domain.Country;
import com.quarkus.training.entity.CountryEntity;
import com.quarkus.training.exception.EntityNotFoundException;
import com.quarkus.training.exception.RequestException;
import com.quarkus.training.repository.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
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

    @Cacheable(key = "#name")
    public Country getCountry(String name) {
        return countryRepository.findByNameIgnoreCase(name).orElseThrow(() ->
                new EntityNotFoundException(String.format("country not found with name = %s", name)))
                .toCountry();
    }

    public Country createCountry(Country country) {
        countryRepository.findByNameIgnoreCase(country.getName())
                .ifPresent(entity -> {
                    throw new RequestException(String.format("the country with name %s is already created", entity.getName()),
                            Response.Status.CONFLICT);
                });
        return countryRepository.save(CountryEntity.fromCountry(country)).toCountry();
    }

    @CachePut(key = "#name")
    public Country updateCountry(String name, Country country) {
        return countryRepository.findByNameIgnoreCase(name)
                .map(entity -> {
                    country.setName(name);
                    return countryRepository.save(CountryEntity.fromCountry(country)).toCountry();
                }).orElseThrow(() -> new EntityNotFoundException(String.format("country not found with name = %s", name)));
    }

    @CacheEvict(key = "#name")
    public void deleteCountry(String name) {
        if(countryRepository.existsById(name)) {
            try {
                countryRepository.deleteById(name);
            } catch (Exception e) {
                throw new RequestException(String.format("the country with name %s cannot be deleted", name),
                        Response.Status.CONFLICT);
            }
        }
    }

}
