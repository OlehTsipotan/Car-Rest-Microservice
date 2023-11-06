package com.myapi.cars.repository;

import com.myapi.cars.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {

    @Query("SELECT c FROM Car c WHERE " +
            "(?1 IS NULL OR c.make.name = ?1)" +
            "AND (?2 IS NULL OR c.year = ?2) " +
            "AND (?3 IS NULL OR c.model = ?3) " +
            "AND (?4 IS NULL OR (SELECT count(*) FROM c.categories cat WHERE cat.name IN (?4)) = ?5)")
    Page<Car> findAll(String makeName, Integer year, String model, List<String> categoryNameList,
                      Integer categoryNameListSize, Pageable pageable);

}