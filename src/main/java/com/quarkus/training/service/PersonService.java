package com.quarkus.training.service;

import com.quarkus.training.config.MessageSource;
import com.quarkus.training.domain.Person;
import com.quarkus.training.exception.EntityNotFoundException;
import com.quarkus.training.exception.RequestException;
import com.quarkus.training.mapping.PersonMapper;
import com.quarkus.training.repository.CountryRepository;
import com.quarkus.training.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

@ApplicationScoped
@AllArgsConstructor
public class PersonService {

    PersonRepository personRepository;

    CountryRepository countryRepository;

    PersonMapper personMapper;

    MessageSource messageSource;

    public Page<Person> getPersons(Pageable pageable) {
        return personRepository.findAll(pageable).map(personMapper::toPerson);
    }

    public Person getPerson(Long id) {
        return personMapper.toPerson(personRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("person.notfound", id))));
    }

    @Transactional
    public Person createPerson(Person person) {
        countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("country.notfound", person.getCountry().getName())));
        person.setId(null);
        return personMapper.toPerson(personRepository.save(personMapper.fromPerson(person)));
    }

    @Transactional
    public Person updatePerson(Long id, Person person) {
        return personRepository.findById(id)
                .map(entity -> {
                    countryRepository.findByNameIgnoreCase(person.getCountry().getName()).orElseThrow(() ->
                            new EntityNotFoundException(messageSource.getMessage("country.notfound", person.getCountry().getName())));
                    person.setId(id);
                    return personMapper.toPerson(personRepository.save(personMapper.fromPerson(person)));
                }).orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("person.notfound", id)));
    }

    @Transactional
    public void deletePerson(Long id) {
        try {
            personRepository.deleteById(id);
        } catch (Exception e) {
            throw new RequestException(messageSource.getMessage("person.errordeletion", id),
                    Response.Status.CONFLICT);
        }
    }

}