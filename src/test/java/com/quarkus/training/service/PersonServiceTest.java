package com.quarkus.training.service;

import com.quarkus.training.BaseTestClass;
import com.quarkus.training.domain.Person;
import com.quarkus.training.entity.PersonEntity;
import com.quarkus.training.exception.EntityNotFoundException;
import com.quarkus.training.exception.RequestException;
import com.quarkus.training.mapping.CountryMapper;
import com.quarkus.training.mapping.PersonMapper;
import com.quarkus.training.repository.CountryRepository;
import com.quarkus.training.repository.PersonRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@QuarkusTest
class PersonServiceTest extends BaseTestClass {

    @InjectMock
    PersonRepository personRepository;

    @InjectMock
    CountryRepository countryRepository;

    @Inject
    PersonService personService;

    @Inject
    PersonMapper personMapper;
    
    @Inject
    CountryMapper countryMapper;
    
    @Test
    void testGetPersons() {
        List<Person> persons = Collections.singletonList(getPerson());
        Pageable pageable = PageRequest.of(1, 5);
        Page<PersonEntity> page = new PageImpl<>(persons.stream().map(personMapper::fromPerson).collect(Collectors.toList()), pageable, persons.size());
        given(personRepository.findAll(pageable)).willReturn(page);
        Page<Person> result = personService.getPersons(pageable);
        verify(personRepository).findAll(pageable);
        assertThat(result.getContent().size()).isEqualTo(persons.size());
    }

    @Test
    void testGetPerson() {
        // test get existing person
        Person person = getPerson();
        given(personRepository.findById(person.getId())).willReturn(Optional.of(personMapper.fromPerson(person)));
        Person result = personService.getPerson(person.getId());
        verify(personRepository).findById(person.getId());
        assertThat(result).isEqualTo(person);

        // test get non existing person
        reset(personRepository);
        Long id = 2L;
        assertThatThrownBy(() -> personService.getPerson(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("person not found with id = %d", id));
    }

    @Test
    void testCreatePersonCountry() {
        // test create person with existing country
        Person person = getPerson();
        given(countryRepository.findByNameIgnoreCase(anyString()))
                .willReturn(Optional.of(countryMapper.fromCountry(person.getCountry())));
        given(personRepository.save(any())).willReturn(personMapper.fromPerson(person));
        Person result = personService.createPerson(getPerson());
        verify(countryRepository).findByNameIgnoreCase(anyString());
        verify(personRepository).save(any());
        assertThat(person).isEqualTo(result);

        // test create with non existing country
        reset(countryRepository);
        given(countryRepository.findByNameIgnoreCase(anyString()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> personService.createPerson(person))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("country not found with name = %s", person.getCountry().getName()));
    }


    @Test
    void testUpdatePerson() {
        // test update person with existing country
        Person person = getPerson();
        given(personRepository.findById(person.getId())).willReturn(Optional.of(personMapper.fromPerson(person)));
        given(countryRepository.findByNameIgnoreCase(anyString()))
                .willReturn(Optional.of(countryMapper.fromCountry(person.getCountry())));
        given(personRepository.save(any())).willReturn(personMapper.fromPerson(person));
        Person result = personService.updatePerson(person.getId(), getPerson());
        verify(countryRepository).findByNameIgnoreCase(anyString());
        verify(personRepository).save(any());
        assertThat(person).isEqualTo(result);

        // test update person with non existing country
        reset(countryRepository);
        given(countryRepository.findByNameIgnoreCase(anyString()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> personService.updatePerson(person.getId(), getPerson()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("country not found with name = %s", person.getCountry().getName()));

        // test update non existing person
        reset(personRepository);
        Long id = 2L;
        assertThatThrownBy(() -> personService.updatePerson(id, getPerson()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("person not found with id = %s", id));

    }

    @Test
    void testDeletePerson() {
        Long id = 1L;
        personService.deletePerson(id);
        verify(personRepository).deleteById(id);

        // test person cannot be deleted
        doThrow(RuntimeException.class).when(personRepository).deleteById(id);
        assertThatThrownBy(() -> personService.deletePerson(id))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining(String.format("the person with id %s cannot be deleted", id))
                .hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
    }

}