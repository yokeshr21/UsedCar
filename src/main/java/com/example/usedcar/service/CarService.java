package com.example.usedcar.service;

import com.example.usedcar.model.Car;
import com.example.usedcar.repository.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CarService {
    private final CarRepository repository;

    public CarService(CarRepository repository) {
        this.repository = repository;
    }

    public Page<Car> getAllCars(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Car> searchCars(String make, String model, Double minPrice, Double maxPrice) {
        return repository.searchCars(make, model, minPrice, maxPrice);
    }

    public List<Car> getAllCars() {
        return repository.findAll();
    }

    public Car getCarById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Car addCar(Car car) {
        return repository.save(car);
    }

    public Car updateCar(Long id, Car updatedCar) {
        return repository.findById(id).map(car -> {

            car.setMake(updatedCar.getMake());

            car.setModel(updatedCar.getModel());

            car.setYear(updatedCar.getYear());

            car.setPrice(updatedCar.getPrice());

            car.setMileage(updatedCar.getMileage());

            car.setDescription(updatedCar.getDescription());

            return repository.save(car);
        }).orElse(null);
    }

    public void deleteCar(Long id) {
        repository.deleteById(id);
    }
}
