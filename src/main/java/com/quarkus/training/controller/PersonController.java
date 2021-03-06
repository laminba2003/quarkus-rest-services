package com.quarkus.training.controller;

import com.quarkus.training.config.Logged;
import com.quarkus.training.domain.Person;
import com.quarkus.training.service.PersonService;
import io.quarkus.security.Authenticated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/persons")
@Authenticated
@Logged
public class PersonController {

    @Inject
    PersonService personService;

    @GET
    public Page<Person> getPersons(@DefaultValue("1") @QueryParam("page") int page,
                                   @DefaultValue("10") @QueryParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return personService.getPersons(pageable);
    }

    @Path("{id}")
    @GET
    public Person getPerson(@PathParam("id") Long id) {
        return personService.getPerson(id);
    }

    @POST
    @RolesAllowed("admin")
    public Response createPerson(@Valid Person person) {
        return Response.ok(personService.createPerson(person))
                .status(Response.Status.CREATED).build();
    }

    @Path("{id}")
    @PUT
    @RolesAllowed("admin")
    public Person updatePerson(@PathParam("id") Long id, @Valid Person person) {
        return personService.updatePerson(id, person);
    }

    @Path("{id}")
    @DELETE
    @RolesAllowed("admin")
    public Response deletePerson(@PathParam("id") Long id) {
        personService.deletePerson(id);
        return Response.ok().status(Response.Status.OK).build();
    }

}