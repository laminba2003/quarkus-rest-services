package com.quarkus.training.controller;

import com.quarkus.training.domain.Country;
import com.quarkus.training.service.CountryService;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/countries")
public class CountryController {

    @Inject
    private CountryService countryService;

    @GET
    public List<Country> getCountries() {
        return countryService.getCountries();
    }
}
