package com.example.usedcar.repository;

import com.example.usedcar.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    Page<Car> findByMakeContainingIgnoreCaseOrModelContainingIgnoreCase(String make, String model, Pageable pageable);

    Page<Car> findByYear(int year, Pageable pageable);

    Page<Car> findByPriceBetween(double min, double max, Pageable pageable);

    @Query(value = "SELECT * FROM cars c " +
    "WHERE (:make IS NULL OR LOWER(c.make) LIKE LOWER(CONCAT('%', :make, '%'))) " +
    "AND (:model IS NULL OR LOWER(c.model) LIKE LOWER(CONCAT('%', :model, '%'))) " +
    "AND (:minPrice IS NULL OR c.price >= :minPrice) " +
    "AND (:maxPrice IS NULL OR c.price <= :maxPrice)", nativeQuery = true)
    Page<Car> searchCars(@Param("make") String make,
                         @Param("model") String model,
                         @Param("minPrice") Double minPrice,
                         @Param("maxPrice") Double maxPrice,
                         Pageable pageable);
}
