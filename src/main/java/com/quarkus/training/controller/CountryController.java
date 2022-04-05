package com.quarkus.training.controller;

import com.quarkus.training.domain.Country;
import com.quarkus.training.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import java.util.List;

@Path("/countries")
@Slf4j
public class CountryController {

    @Inject
    CountryService countryService;

    @GET
    public List<Country> getCountries() {
        log.debug("returning the list of countries");
        return countryService.getCountries();
    }

    @Path("{name}")
    @GET
    public Country getCountry(@PathParam("name") String name) {
        log.debug("returning the country with name = {}", name);
        return countryService.getCountry(name);
    }

    @POST
    public Country createCountry(@Valid Country country) {
        log.debug("creating country with values = {} and user : {}", country, null);
        return countryService.createCountry(country);
    }

    @Path("{name}")
    @PUT
    public Country updateCountry(@PathParam("name") String name, @Valid Country country) {
        log.debug("updating country with name = {}, values = {} and user : {}", name, country, null);
        return countryService.updateCountry(name, country);
    }

    @Path("{name}")
    @DELETE
    public void deleteCountry(@PathParam("name") String name) {
        log.debug("deleting country with name = {} and user : {}", name, null);
        countryService.deleteCountry(name);
    }

}