package com.quarkus.training.controller;

import com.quarkus.training.domain.Person;
import com.quarkus.training.service.PersonService;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/persons")
public class PersonController {

    @Inject
    private PersonService personService;

    @GET
    public List<Person> getPersons() {
        return personService.getPersons();
    }
}
