package com.myapi.cars.repository;

import com.myapi.cars.model.Make;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MakeRepositoryTest {

    private static final String DATABASE_NAME = "databaseName";
    private static final String DATABASE_USERNAME = "databaseName";
    private static final String DATABASE_USER_PASSWORD = "databaseName";

    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:latest").withDatabaseName(DATABASE_NAME).withUsername(DATABASE_USERNAME)
                    .withPassword(DATABASE_USER_PASSWORD).withReuse(true);
    @Autowired
    MakeRepository makeRepository;

    @Autowired
    TestEntityManager entityManager;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // Postgresql
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // Flyway
        registry.add("spring.flyway.cleanDisabled", () -> false);
    }

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void save_success() {
        Make make = new Make("Toyota");

        makeRepository.save(make);

        Make makeFromDb = entityManager.find(Make.class, make.getId());
        assertEquals(make, makeFromDb);
        assertEquals(make.getName(), makeFromDb.getName());
    }

    @Test
    public void findById_success() {
        Make make = new Make("Toyota");

        entityManager.persist(make);

        Make makeFromDb = makeRepository.findById(make.getId()).orElse(null);

        assertNotNull(makeFromDb);
        assertEquals(make, makeFromDb);
    }

    @Test
    public void findByName_success() {
        Make make = new Make("Toyota");

        entityManager.persist(make);

        Make makeFromDb = makeRepository.findByName(make.getName()).orElse(null);

        assertNotNull(makeFromDb);
        assertEquals(make, makeFromDb);
    }

    @Test
    public void deleteById_success() {
        Make make = new Make("Toyota");

        entityManager.persist(make);

        makeRepository.deleteById(make.getId());

        Make makeFromDb = entityManager.find(Make.class, make.getId());
        assertNull(makeFromDb);
        assertEquals(0, makeRepository.findAll().size());
    }

    @Test
    public void delete_success() {
        Make make = new Make("Toyota");

        entityManager.persist(make);

        makeRepository.delete(make);

        Make makeFromDb = entityManager.find(Make.class, make.getId());
        assertNull(makeFromDb);
        assertEquals(0, makeRepository.findAll().size());
    }


}
