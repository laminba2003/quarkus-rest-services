package com.quarkus.training.controller;

import com.quarkus.training.domain.Person;
import com.quarkus.training.domain.User;
import com.quarkus.training.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/persons")
@Slf4j
public class PersonController {

    @Inject
    PersonService personService;

    @Inject
    User user;
    
    @GET
    public Page<Person> getPersons(@DefaultValue("1") @QueryParam("page") int page,
                                   @DefaultValue("10") @QueryParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        log.debug("returning the list of persons with {}", pageable);
        return personService.getPersons(pageable);
    }

    @Path("{id}")
    @GET
    public Person getPerson(@PathParam("id") Long id) {
        log.debug("returning the person with id = {}", id);
        return personService.getPerson(id);
    }

    @POST
    @RolesAllowed("admin")
    public Response createPerson(@Valid Person person) {
        log.debug("creating person with values = {} and user : {}", person, user.getEmail());
        return Response.ok(personService.createPerson(person))
                .status(Response.Status.CREATED).build();
    }

    @Path("{id}")
    @PUT
    @RolesAllowed("admin")
    public Person updatePerson(@PathParam("id") Long id, @Valid Person person) {
        log.debug("updating person with id = {}, values = {} and user : {}", id, person, user.getEmail());
        return personService.updatePerson(id, person);
    }

    @Path("{id}")
    @DELETE
    @RolesAllowed("admin")
    public void deletePerson(@PathParam("id") Long id) {
        log.debug("deleting person with id = {} and user : {}", id, user.getEmail());
        personService.deletePerson(id);
    }

}