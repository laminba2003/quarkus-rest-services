package com.quarkus.training.mapping;

import com.quarkus.training.domain.Country;
import com.quarkus.training.entity.CountryEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CountryMapper {

    Country toCountry(CountryEntity countryEntity);

    CountryEntity fromCountry(Country country);

}
