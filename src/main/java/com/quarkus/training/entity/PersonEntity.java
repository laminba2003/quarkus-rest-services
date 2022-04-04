package com.quarkus.training.entity;

import com.quarkus.training.domain.Person;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name="persons")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @ManyToOne
    private CountryEntity country;

    public Person toPerson() {
        return new Person(id, firstName, lastName, country.toCountry());
    }

    public static PersonEntity fromPerson(Person person) {
        PersonEntity entity = new PersonEntity();
        entity.setId(person.getId());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setCountry(CountryEntity.fromCountry(person.getCountry()));
        return entity;
    }

}
