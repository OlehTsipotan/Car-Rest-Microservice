package com.myapi.cars.repository;

import com.myapi.cars.model.Category;
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
public class CategoryRepositoryTest {

    private static final String DATABASE_NAME = "databaseName";
    private static final String DATABASE_USERNAME = "databaseName";
    private static final String DATABASE_USER_PASSWORD = "databaseName";

    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:latest").withDatabaseName(DATABASE_NAME).withUsername(DATABASE_USERNAME)
                    .withPassword(DATABASE_USER_PASSWORD).withReuse(true);
    @Autowired
    CategoryRepository categoryRepository;

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
        Category category = new Category("Sedan");

        categoryRepository.save(category);

        Category categoryFromDB = entityManager.find(Category.class, category.getId());
        assertEquals(category, categoryFromDB);
        assertEquals(category.getName(), category.getName());
    }

    @Test
    public void findById_success() {
        Category category = new Category("Sedan");

        entityManager.persist(category);

        Category categoryFromDB = categoryRepository.findById(category.getId()).orElse(null);

        assertNotNull(categoryFromDB);
        assertEquals(category, categoryFromDB);
    }

    @Test
    public void findByName_success() {

        Category category = new Category("Sedan");
        entityManager.persist(category);

        Category categoryFromDB = categoryRepository.findByName(category.getName()).orElse(null);

        assertNotNull(categoryFromDB);
        assertEquals(category, categoryFromDB);
    }

    @Test
    public void deleteById_success() {
        Category category = new Category("Sedan");

        entityManager.persist(category);

        categoryRepository.deleteById(category.getId());

        Category categoryFromDb = entityManager.find(Category.class, category.getId());
        assertNull(categoryFromDb);
        assertEquals(0, categoryRepository.findAll().size());
    }

    @Test
    public void delete_success() {
        Category category = new Category("Sedan");

        entityManager.persist(category);

        categoryRepository.delete(category);

        Category categoryFromDb = entityManager.find(Category.class, category.getId());
        assertNull(categoryFromDb);
        assertEquals(0, categoryRepository.findAll().size());
    }


}
