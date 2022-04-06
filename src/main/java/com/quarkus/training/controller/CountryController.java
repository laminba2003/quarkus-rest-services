package com.quarkus.training.controller;

import com.quarkus.training.domain.Country;
import com.quarkus.training.domain.User;
import com.quarkus.training.service.CountryService;
import io.quarkus.security.Authenticated;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/countries")
@Slf4j
@Authenticated
public class CountryController {

    @Inject
    CountryService countryService;

    @Inject
    User user;

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
    @RolesAllowed("admin")
    public Response createCountry(@Valid Country country, @Context HttpServerResponse response) {
        log.debug("creating country with values = {} and user : {}", country, user.getEmail());
        return Response.ok(countryService.createCountry(country))
                .status(Response.Status.CREATED).build();
    }

    @Path("{name}")
    @PUT
    @RolesAllowed("admin")
    public Country updateCountry(@PathParam("name") String name, @Valid Country country) {
        log.debug("updating country with name = {}, values = {} and user : {}", name, country, user.getEmail());
        return countryService.updateCountry(name, country);
    }

    @Path("{name}")
    @DELETE
    @RolesAllowed("admin")
    public void deleteCountry(@PathParam("name") String name) {
        log.debug("deleting country with name = {} and user : {}", name, user.getEmail());
        countryService.deleteCountry(name);
    }

}