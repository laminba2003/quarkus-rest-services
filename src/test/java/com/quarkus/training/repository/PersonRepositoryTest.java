package com.quarkus.training.repository;

import com.quarkus.training.MySQLResource;
import com.quarkus.training.entity.PersonEntity;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import javax.inject.Inject;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(MySQLResource.class)
class PersonRepositoryTest {

    @Inject
    PersonRepository personRepository;

    @Test
    void testFindAll() {
        Page<PersonEntity> entities = personRepository.findAll(PageRequest.of(0,2));
        assertThat(entities.getContent().size()).isEqualTo(2);
    }
}