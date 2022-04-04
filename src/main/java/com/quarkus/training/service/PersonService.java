package com.quarkus.training.service;

import com.quarkus.training.domain.Person;
import com.quarkus.training.entity.PersonEntity;
import com.quarkus.training.repository.PersonRepository;
import lombok.AllArgsConstructor;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> getPersons() {
        return StreamSupport.stream(personRepository.findAll().spliterator(), false)
                .map(PersonEntity::toPerson)
                .collect(Collectors.toList());
    }
}
