package com.quarkus.training.domain;

import lombok.Data;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
@Data
public class User {
    @Inject
    @Claim(standard = Claims.given_name)
    String firstName;
    @Inject
    @Claim(standard = Claims.family_name)
    String lastName;
    @Inject
    @Claim(standard = Claims.email)
    String email;
}
