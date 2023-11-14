package com.myapi.cars.repository;

import com.myapi.cars.model.Car;
import com.myapi.cars.model.Category;
import com.myapi.cars.model.Make;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarRepositoryTest {

    private static final String DATABASE_NAME = "databaseName";
    private static final String DATABASE_USERNAME = "databaseName";
    private static final String DATABASE_USER_PASSWORD = "databaseName";

    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:latest").withDatabaseName(DATABASE_NAME).withUsername(DATABASE_USERNAME)
                    .withPassword(DATABASE_USER_PASSWORD).withReuse(true);
    @Autowired
    CarRepository carRepository;

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
        entityManager.persist(make);

        Category category = new Category("Sedan");
        entityManager.persist(category);

        Car car = Car.builder().make(make).categories(Set.of(category)).year(2021).model("Camry").build();

        carRepository.save(car);

        Car carFromDb = entityManager.find(Car.class, car.getId());
        assertEquals(car, carFromDb);
        assertEquals(car.getMake(), carFromDb.getMake());
        assertEquals(car.getCategories(), carFromDb.getCategories());
        assertEquals(car.getYear(), carFromDb.getYear());
        assertEquals(car.getModel(), carFromDb.getModel());
    }

    @Test
    public void findById_success() {
        Make make = new Make("Toyota");
        entityManager.persist(make);

        Category category = new Category("Sedan");
        entityManager.persist(category);

        Car car = Car.builder().make(make).categories(Set.of(category)).year(2021).model("Camry").build();

        entityManager.persist(car);

        Car carFromDb = carRepository.findById(car.getId()).orElse(null);
        assertEquals(car, carFromDb);
        assertEquals(car.getMake(), carFromDb.getMake());
        assertEquals(car.getCategories(), carFromDb.getCategories());
        assertEquals(car.getYear(), carFromDb.getYear());
        assertEquals(car.getModel(), carFromDb.getModel());
    }

    @Test
    public void deleteById_success() {
        Make make = new Make("Toyota");
        entityManager.persist(make);

        Category category = new Category("Sedan");
        entityManager.persist(category);

        Car car = Car.builder().make(make).categories(Set.of(category)).year(2021).model("Camry").build();

        entityManager.persist(car);

        carRepository.deleteById(car.getId());

        Car carFromDb = entityManager.find(Car.class, car.getId());
        assertNull(carFromDb);
        assertEquals(0, carRepository.findAll().size());
    }

    @Test
    public void delete_success() {
        Make make = new Make("Toyota");
        entityManager.persist(make);

        Category category = new Category("Sedan");
        entityManager.persist(category);

        Car car = Car.builder().make(make).categories(Set.of(category)).year(2021).model("Camry").build();

        entityManager.persist(car);

        carRepository.delete(car);

        Car carFromDb = entityManager.find(Car.class, car.getId());
        assertNull(carFromDb);
        assertEquals(0, carRepository.findAll().size());
    }

    @Test
    public void findAll_withNullParams_success() {
        Make make = new Make("Toyota");
        entityManager.persist(make);

        Category category = new Category("Sedan");
        entityManager.persist(category);

        Car car1 = Car.builder().make(make).categories(Set.of(category)).year(2021).model("Camry").build();
        Car car2 = Car.builder().make(make).categories(Set.of(category)).year(2022).model("Camry").build();
        Car car3 = Car.builder().make(make).categories(Set.of(category)).year(2023).model("Camry").build();

        entityManager.persist(car1);
        entityManager.persist(car2);
        entityManager.persist(car3);

        assertEquals(3,
                carRepository.findAll(null, null, null, null, null, Pageable.unpaged()).stream().toList().size());
    }

    @Test
    public void findAll_withParams1_success() {
        Make make = new Make("Toyota");
        entityManager.persist(make);

        Category category = new Category("Sedan");
        entityManager.persist(category);

        Car car1 = Car.builder().make(make).categories(Set.of(category)).year(2021).model("Camry").build();
        Car car2 = Car.builder().make(make).categories(Set.of(category)).year(2022).model("Corolla").build();
        Car car3 = Car.builder().make(make).categories(Set.of(category)).year(2023).model("Corolla").build();
        Car car4 = Car.builder().make(make).categories(Set.of(category)).year(2023).model("Land Cruiser").build();

        entityManager.persist(car1);
        entityManager.persist(car2);
        entityManager.persist(car3);
        entityManager.persist(car4);

        List<Car> foundedCars =
                carRepository.findAll(null, null, "Corolla", null, null, Pageable.unpaged()).stream().toList();

        assertEquals(2, foundedCars.size());
        assertTrue(foundedCars.contains(car2));
        assertTrue(foundedCars.contains(car3));
    }

    @Test
    public void findAll_withParams2_success() {
        Make make = new Make("Toyota");
        entityManager.persist(make);

        Category sedan = new Category("Sedan");
        Category dropTop = new Category("Drop-top");
        entityManager.persist(sedan);
        entityManager.persist(dropTop);

        Car car1 = Car.builder().make(make).categories(Set.of(sedan)).year(2021).model("Camry").build();
        Car car2 = Car.builder().make(make).categories(Set.of(sedan)).year(2022).model("Corolla").build();
        Car car3 = Car.builder().make(make).categories(Set.of(dropTop)).year(2023).model("Corolla").build();
        Car car4 = Car.builder().make(make).categories(Set.of(sedan)).year(2023).model("Land Cruiser").build();

        entityManager.persist(car1);
        entityManager.persist(car2);
        entityManager.persist(car3);
        entityManager.persist(car4);

        List<String> categoryNames = Collections.singletonList(dropTop.getName());

        List<Car> foundedCars =
                carRepository.findAll(make.getName(), 2023, "Corolla", categoryNames, categoryNames.size(),
                        Pageable.unpaged()).stream().toList();

        assertEquals(1, foundedCars.size());
        assertTrue(foundedCars.contains(car3));
    }

    @Test
    public void findAll_whenCategoryNameListIsNotNullAndSizeIsNull_success() {
        Make make = new Make("Toyota");
        entityManager.persist(make);

        Category sedan = new Category("Sedan");
        Category dropTop = new Category("Drop-top");
        entityManager.persist(sedan);
        entityManager.persist(dropTop);

        Car car1 = Car.builder().make(make).categories(Set.of(sedan)).year(2021).model("Camry").build();
        Car car2 = Car.builder().make(make).categories(Set.of(sedan)).year(2022).model("Corolla").build();
        Car car3 = Car.builder().make(make).categories(Set.of(dropTop)).year(2023).model("Corolla").build();
        Car car4 = Car.builder().make(make).categories(Set.of(sedan)).year(2023).model("Land Cruiser").build();

        entityManager.persist(car1);
        entityManager.persist(car2);
        entityManager.persist(car3);
        entityManager.persist(car4);

        List<String> categoryNames = Collections.singletonList(dropTop.getName());

        List<Car> foundedCars =
                carRepository.findAll(make.getName(), 2023, "Corolla", categoryNames, null, Pageable.unpaged()).stream()
                        .toList();

        assertEquals(0, foundedCars.size());
    }

    @Test
    public void findAll_whenCategoryNameListHasMultipleElements_success() {
        Make make = new Make("Toyota");
        entityManager.persist(make);

        Category sedan = new Category("Sedan");
        Category dropTop = new Category("Drop-top");
        Category sport = new Category("sport");
        entityManager.persist(sedan);
        entityManager.persist(dropTop);
        entityManager.persist(sport);

        Car car1 = Car.builder().make(make).categories(Set.of(sedan)).year(2021).model("Camry").build();
        Car car2 = Car.builder().make(make).categories(Set.of(sedan)).year(2022).model("Corolla").build();
        Car car3 = Car.builder().make(make).categories(Set.of(dropTop, sedan)).year(2023).model("Corolla").build();
        Car car4 =
                Car.builder().make(make).categories(Set.of(dropTop, sedan, sport)).year(2023).model("Corolla").build();
        Car car5 = Car.builder().make(make).categories(Set.of(sedan)).year(2023).model("Land Cruiser").build();

        entityManager.persist(car1);
        entityManager.persist(car2);
        entityManager.persist(car3);
        entityManager.persist(car4);
        entityManager.persist(car5);

        List<String> categoryNames = Arrays.asList(dropTop.getName(), sedan.getName());

        List<Car> foundedCars =
                carRepository.findAll(make.getName(), 2023, "Corolla", categoryNames, categoryNames.size(),
                        Pageable.unpaged()).stream().toList();

        assertEquals(2, foundedCars.size());
    }


}
