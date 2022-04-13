package com.quarkus.training.service;

import com.quarkus.training.domain.Person;
import com.quarkus.training.exception.EntityNotFoundException;
import com.quarkus.training.mapping.PersonMapper;
import com.quarkus.training.repository.CountryRepository;
import com.quarkus.training.repository.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PersonService {

    @Inject
    PersonRepository personRepository;

    @Inject
    CountryRepository countryRepository;

    @Inject
    PersonMapper mapper;

    public Page<Person> getPersons(Pageable pageable) {
        return personRepository.findAll(pageable).map(mapper::toPerson);
    }

    public Person getPerson(Long id) {
        return mapper.toPerson(personRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("person not found with id = %d", id))));
    }

    public Person createPerson(Person person) {
        countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                new EntityNotFoundException(String.format("country not found with name = %s", person.getCountry().getName())));
        person.setId(null);
        return mapper.toPerson(personRepository.save(mapper.fromPerson(person)));
    }

    public Person updatePerson(Long id, Person person) {
        return personRepository.findById(id)
                .map(entity -> {
                    countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                            new EntityNotFoundException(String.format("country not found with name = %s", person.getCountry().getName())));
                    person.setId(id);
                    return mapper.toPerson(personRepository.save(mapper.fromPerson(person)));
                }).orElseThrow(() -> new EntityNotFoundException(String.format("person not found with id = %d", id)));
    }

    public void deletePerson(Long id) {
        if(personRepository.existsById(id)) {
            personRepository.deleteById(id);
        }
    }

}