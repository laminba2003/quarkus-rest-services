package com.quarkus.training.mapping;

import com.quarkus.training.domain.Person;
import com.quarkus.training.entity.PersonEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface PersonMapper {

    Person toPerson(PersonEntity PersonEntity);

    PersonEntity fromPerson(Person Person);

}
