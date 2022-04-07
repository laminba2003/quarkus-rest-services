package com.quarkus.training.exception;

import com.quarkus.training.BaseTestClass;
import com.quarkus.training.controller.PersonController;
import com.quarkus.training.service.PersonService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.NotFoundException;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static javax.ws.rs.core.Response.Status.*;

@QuarkusTest
@TestHTTPEndpoint(value = PersonController.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class APIExceptionHandlerTest extends BaseTestClass {

    @InjectMock
    PersonService personService;

    @Test
    public void testHandleRequestException() {
        RequestException exception = new RequestException("error message",
                INTERNAL_SERVER_ERROR);
        when(personService.getPerson(any()))
                .thenThrow(exception);
        RestAssured.given().auth().oauth2(getToken())
                .pathParam("id", 1L)
                .when().get("{id}")
                .then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode())
                .body("message", equalTo("error message") );
    }

    @Test
    public void testHandleEntityNotFoundException()  {
        EntityNotFoundException exception = new EntityNotFoundException("person not found with id=1");
        when(personService.getPerson(any()))
                .thenThrow(exception);
        RestAssured.given().auth().oauth2(getToken())
                .pathParam("id", 1L)
                .when().get("{id}")
                .then()
                .statusCode(NOT_FOUND.getStatusCode())
                .body("message", equalTo("person not found with id=1") );
    }

    @Test
    public void testHandleConstraintViolationException() {
        ConstraintViolationException exception = new ConstraintViolationException(null);
        when(personService.getPerson(any()))
                .thenThrow(exception);
        RestAssured.given().auth().oauth2(getToken())
                .pathParam("id", 1L)
                .when().get("{id}")
                .then()
                .statusCode(BAD_REQUEST.getStatusCode())
                .body("message", equalTo("invalid input") );
    }

    @Test
    public void testNotFoundException() {
        NotFoundException exception = new NotFoundException();
        when(personService.getPerson(any()))
                .thenThrow(exception);
        RestAssured.given().auth().oauth2(getToken())
                .when().get("ppppp")
                .then()
                .statusCode(NOT_FOUND.getStatusCode())
                .body("message", equalTo(NOT_FOUND.getReasonPhrase()) );
    }

    @Test
    public void testHandleRuntimeException() {
        RuntimeException exception = new RuntimeException();
        when(personService.getPerson(any()))
                .thenThrow(exception);
        RestAssured.given().auth().oauth2(getToken())
                .pathParam("id", 1L)
                .when().get("{id}")
                .then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode())
                .body("message", equalTo("internal server error") );
    }

}