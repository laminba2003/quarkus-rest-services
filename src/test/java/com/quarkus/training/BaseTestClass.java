package com.quarkus.training;

import com.quarkus.training.domain.Country;
import com.quarkus.training.domain.Person;
import io.smallrye.jwt.build.Jwt;
import java.util.Collections;
import java.util.Set;

public abstract class BaseTestClass {

    protected Person getPerson() {
        return new Person(1L, "Mamadou Lamine", "Ba", getCountry());
    }

    protected Country getCountry() {
        return new Country("France", "Paris", 1223333677);
    }

    protected String getToken() {
        Set<String> roles = Collections.singleton("admin");
        return Jwt.preferredUserName("john")
                .issuer("https://server.example.com")
                .audience("https://service.example.com")
                .claim("roles", roles)
                .claim("email", "johndoe@example.com")
                .claim("given_name", "john")
                .claim("family_name", "doe")
                .sign();
    }

}
