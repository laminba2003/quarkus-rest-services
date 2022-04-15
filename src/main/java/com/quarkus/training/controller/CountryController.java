package com.quarkus.training.controller;

import com.quarkus.training.config.Logged;
import com.quarkus.training.domain.Country;
import com.quarkus.training.service.CountryService;
import io.quarkus.security.Authenticated;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/countries")
@Authenticated
@Logged
public class CountryController {

    @Inject
    CountryService countryService;

    @GET
    public List<Country> getCountries() {
        return countryService.getCountries();
    }

    @Path("{name}")
    @GET
    public Country getCountry(@PathParam("name") String name) {
        return countryService.getCountry(name);
    }

    @POST
    @RolesAllowed("admin")
    public Response createCountry(@Valid Country country) {
        return Response.ok(countryService.createCountry(country))
                .status(Response.Status.CREATED).build();
    }

    @Path("{name}")
    @PUT
    @RolesAllowed("admin")
    public Country updateCountry(@PathParam("name") String name, @Valid Country country) {
        return countryService.updateCountry(name, country);
    }

    @Path("{name}")
    @DELETE
    @RolesAllowed("admin")
    public Response deleteCountry(@PathParam("name") String name) {
        countryService.deleteCountry(name);
        return Response.ok().status(Response.Status.OK).build();
    }

}