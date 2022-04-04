package com.quarkus.training.repository;

import com.quarkus.training.entity.CountryEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<CountryEntity, String> {

    Optional<CountryEntity> findByNameIgnoreCase(String name);

}
