package com.example.usedcar.repository;

import com.example.usedcar.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByMakeContainingIgnoreCaseOrModelContainingIgnoreCase(String make, String model);

    List<Car> findByYear(int year);

    List<Car> findByPriceBetween(double min, double max);

    @Query("SELECT c FROM Car c " +
    "WHERE (:make IS NULL OR LOWER(CAST(c.make AS string)) LIKE LOWER(CONCAT('%', :make, '%'))) " +
    "AND (:model IS NULL OR LOWER(CAST(c.model AS string)) LIKE LOWER(CONCAT('%', :model, '%'))) " +
    "AND (:minPrice IS NULL OR c.price >= :minPrice) " +
    "AND (:maxPrice IS NULL OR c.price <= :maxPrice)")
    List<Car> searchCars(@Param("make") String make,
                         @Param("model") String model,
                         @Param("minPrice") Double minPrice,
                         @Param("maxPrice") Double maxPrice);
}
