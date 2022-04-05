package com.quarkus.training.service;

import com.quarkus.training.domain.Person;
import com.quarkus.training.entity.PersonEntity;
import com.quarkus.training.exception.EntityNotFoundException;
import com.quarkus.training.repository.CountryRepository;
import com.quarkus.training.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final CountryRepository countryRepository;

    public Page<Person> getPersons(Pageable pageable) {
        return personRepository.findAll(pageable).map(PersonEntity::toPerson);
    }

    public Person getPerson(Long id) {
        return personRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("person not found with id = %d", id)))
                .toPerson();
    }

    public Person createPerson(Person person) {
        person.setId(null);
        person.setCountry(countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                new EntityNotFoundException(String.format("country not found with name = %s", person.getCountry().getName()))).toCountry());
        return personRepository.save(PersonEntity.fromPerson(person)).toPerson();
    }

    public Person updatePerson(Long id, Person person) {
        return personRepository.findById(id)
                .map(entity -> {
                    person.setId(id);
                    person.setCountry(countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                            new EntityNotFoundException(String.format("country not found with name = %s", person.getCountry().getName()))).toCountry());
                    return personRepository.save(PersonEntity.fromPerson(person)).toPerson();
                }).orElseThrow(() -> new EntityNotFoundException(String.format("person not found with id = %d", id)));
    }

    public void deletePerson(Long id) {
        if(personRepository.existsById(id)) {
            personRepository.deleteById(id);
        }
    }

}