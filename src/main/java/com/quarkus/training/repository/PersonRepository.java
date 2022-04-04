package com.quarkus.training.repository;

import com.quarkus.training.entity.PersonEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PersonRepository extends PagingAndSortingRepository<PersonEntity, Long> {

}