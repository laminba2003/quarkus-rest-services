package com.quarkus.training.controller;

import com.quarkus.training.BaseTestClass;
import com.quarkus.training.domain.Person;
import com.quarkus.training.service.PersonService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static javax.ws.rs.core.Response.Status.*;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@QuarkusTest
@TestHTTPEndpoint(value = PersonController.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class PersonControllerTest extends BaseTestClass  {

    @InjectMock
    PersonService personService;

    @BeforeAll
    public static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void testGetPersons() {
        List<Person> persons = Collections.singletonList(getPerson());
        Pageable pageable = PageRequest.of(1, 5);
        Page<Person> page = new PageImpl<>(persons, pageable, persons.size());
        given(personService.getPersons(pageable)).willReturn(page);
        RestAssured.given().auth().oauth2(getToken())
                .queryParam("page", 1)
                .queryParam("size", 5)
                .accept(ContentType.JSON)
                .when().get()
                .then()
                .contentType(ContentType.JSON)
                .statusCode(OK.getStatusCode())
                .body("content[0].id", equalTo((persons.get(0).getId().intValue())))
                .body("content[0].firstName", equalTo(persons.get(0).getFirstName()))
                .body("content[0].lastName", equalTo(persons.get(0).getLastName()))
                .body("content[0].country.name", equalTo(persons.get(0).getCountry().getName()))
                .body("content[0].country.capital", equalTo(persons.get(0).getCountry().getCapital()))
                .body("content[0].country.population", equalTo(persons.get(0).getCountry().getPopulation()));
    }

    @Test
    void testGetPerson() {
        Person person = getPerson();
        given(personService.getPerson(person.getId())).willReturn(person);
        RestAssured.given().auth().oauth2(getToken())
                .accept(ContentType.JSON)
                .pathParam("id", person.getId())
                .when().get("{id}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(OK.getStatusCode())
                .body("id", equalTo((person.getId().intValue())))
                .body("firstName", equalTo(person.getFirstName()))
                .body("lastName", equalTo(person.getLastName()))
                .body("country.name", equalTo(person.getCountry().getName()))
                .body("country.capital", equalTo(person.getCountry().getCapital()))
                .body("country.population", equalTo(person.getCountry().getPopulation()));
    }

    @Test
    void testCreatePerson() {
        Person person = getPerson();
        given(personService.createPerson(person)).willReturn(person);
        RestAssured.given().auth().oauth2(getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(person)
                .when().post()
                .then()
                .contentType(ContentType.JSON)
                .statusCode(CREATED.getStatusCode())
                .body("id", equalTo((person.getId().intValue())))
                .body("firstName", equalTo(person.getFirstName()))
                .body("lastName", equalTo(person.getLastName()))
                .body("country.name", equalTo(person.getCountry().getName()))
                .body("country.capital", equalTo(person.getCountry().getCapital()))
                .body("country.population", equalTo(person.getCountry().getPopulation()));
    }

    @Test
    void testUpdatePerson() {
        Person person = getPerson();
        given(personService.updatePerson(person.getId(), person)).willReturn(person);
        RestAssured.given().auth().oauth2(getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(person)
                .pathParam("id", person.getId())
                .when().put("{id}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(OK.getStatusCode())
                .body("id", equalTo((person.getId().intValue())))
                .body("firstName", equalTo(person.getFirstName()))
                .body("lastName", equalTo(person.getLastName()))
                .body("country.name", equalTo(person.getCountry().getName()))
                .body("country.capital", equalTo(person.getCountry().getCapital()))
                .body("country.population", equalTo(person.getCountry().getPopulation()));
    }

    @Test
    void testDeletePerson() {
        Person person = getPerson();
        doNothing().when(personService).deletePerson(person.getId());
        RestAssured.given().auth().oauth2(getToken())
                .pathParam("id", person.getId())
                .when().delete("{id}")
                .then()
                .statusCode(OK.getStatusCode());
    }

}