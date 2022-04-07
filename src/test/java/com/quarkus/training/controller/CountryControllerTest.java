package com.quarkus.training.controller;

import com.quarkus.training.BaseTestClass;
import com.quarkus.training.domain.Country;
import com.quarkus.training.service.CountryService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@QuarkusTest
@TestHTTPEndpoint(value = CountryController.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class CountryControllerTest extends BaseTestClass {

    @InjectMock
    CountryService countryService;

    @BeforeAll
    public static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void testGetCountries() {
        List<Country> countries = Collections.singletonList(getCountry());
        given(countryService.getCountries()).willReturn(countries);
        RestAssured.given().auth().oauth2(getToken())
                .accept(ContentType.JSON)
                .when().get()
                .then()
                .contentType(ContentType.JSON)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name[0]", equalTo(countries.get(0).getName()))
                .body("capital[0]", equalTo(countries.get(0).getCapital()))
                .body("population[0]", equalTo(countries.get(0).getPopulation()));
    }

    @Test
    void testGetCountry() {
        Country country = getCountry();
        given(countryService.getCountry(country.getName())).willReturn(country);
        RestAssured.given().auth().oauth2(getToken())
                .accept(ContentType.JSON)
                .pathParam("name", "France")
                .when().get("{name}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", equalTo(country.getName()))
                .body("capital", equalTo(country.getCapital()))
                .body("population", equalTo(country.getPopulation()));
    }

    @Test
    void testCreateCountry() throws Exception {
        Country country = getCountry();
        given(countryService.createCountry(country)).willReturn(country);
        RestAssured.given().auth().oauth2(getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(country)
                .when().post()
                .then()
                .contentType(ContentType.JSON)
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("name", equalTo(country.getName()))
                .body("capital", equalTo(country.getCapital()))
                .body("population", equalTo(country.getPopulation()));
    }

    @Test
    void testUpdateCountry() {
        Country country = getCountry();
        given(countryService.updateCountry(country.getName(), country)).willReturn(country);
        RestAssured.given().auth().oauth2(getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(country)
                .pathParam("name", "France")
                .when().put("{name}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", equalTo(country.getName()))
                .body("capital", equalTo(country.getCapital()))
                .body("population", equalTo(country.getPopulation()));
    }

    @Test
    void testDeleteCountry() {
        Country country = getCountry();
        doNothing().when(countryService).deleteCountry(country.getName());
        RestAssured.given().auth().oauth2(getToken())
                .pathParam("name", "France")
                .when().delete("{name}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}