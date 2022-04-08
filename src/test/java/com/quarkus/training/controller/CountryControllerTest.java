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
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.restdocs.ManualRestDocumentation;
import java.util.Collections;
import java.util.List;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@QuarkusTest
@TestHTTPEndpoint(value = CountryController.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class CountryControllerTest extends BaseTestClass {

    @InjectMock
    CountryService countryService;
    RequestSpecification specification;
    ManualRestDocumentation restDocumentation;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        restDocumentation = new ManualRestDocumentation("target/snippets/countries");
        specification = new RequestSpecBuilder().
                addFilter(documentationConfiguration(restDocumentation)).build();
        restDocumentation.beforeTest(getClass(), testInfo.getTestMethod().get().getName());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void testGetCountries() {
        List<Country> countries = Collections.singletonList(getCountry());
        given(countryService.getCountries()).willReturn(countries);
        RestAssured.given(specification).auth().oauth2(getToken())
                .accept(ContentType.JSON)
                .filter(document("getCountries"))
                .when().get()
                .then()
                .contentType(ContentType.JSON)
                .statusCode(OK.getStatusCode())
                .body("name[0]", equalTo(countries.get(0).getName()))
                .body("capital[0]", equalTo(countries.get(0).getCapital()))
                .body("population[0]", equalTo(countries.get(0).getPopulation()));

    }

    @Test
    void testGetCountry() {
        Country country = getCountry();
        given(countryService.getCountry(country.getName())).willReturn(country);
        RestAssured.given(specification).auth().oauth2(getToken())
                .accept(ContentType.JSON)
                .pathParam("name", "France")
                .filter(document("getCountry"))
                .when().get("{name}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(OK.getStatusCode())
                .body("name", equalTo(country.getName()))
                .body("capital", equalTo(country.getCapital()))
                .body("population", equalTo(country.getPopulation()));
    }

    @Test
    void testCreateCountry() throws Exception {
        Country country = getCountry();
        given(countryService.createCountry(country)).willReturn(country);
        RestAssured.given(specification).auth().oauth2(getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(country)
                .filter(document("createCountry"))
                .when().post()
                .then()
                .contentType(ContentType.JSON)
                .statusCode(CREATED.getStatusCode())
                .body("name", equalTo(country.getName()))
                .body("capital", equalTo(country.getCapital()))
                .body("population", equalTo(country.getPopulation()));
    }

    @Test
    void testUpdateCountry() {
        Country country = getCountry();
        given(countryService.updateCountry(country.getName(), country)).willReturn(country);
        RestAssured.given(specification).auth().oauth2(getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(country)
                .pathParam("name", "France")
                .filter(document("updateCountry"))
                .when().put("{name}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(OK.getStatusCode())
                .body("name", equalTo(country.getName()))
                .body("capital", equalTo(country.getCapital()))
                .body("population", equalTo(country.getPopulation()));
    }

    @Test
    void testDeleteCountry() {
        Country country = getCountry();
        doNothing().when(countryService).deleteCountry(country.getName());
        RestAssured.given(specification).auth().oauth2(getToken())
                .pathParam("name", "France")
                .filter(document("deleteCountry"))
                .when().delete("{name}")
                .then()
                .statusCode(OK.getStatusCode());
    }
}