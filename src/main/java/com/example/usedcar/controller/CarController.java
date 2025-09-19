package com.example.usedcar.controller;

import com.example.usedcar.model.Car;
import com.example.usedcar.service.CarService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "http://localhost:5173")
public class CarController {
    private final CarService service;

    public CarController(CarService service) {
        this.service = service;
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadCarImage(@PathVariable Long id,
                                            @RequestParam("image")MultipartFile file,
                                            HttpServletRequest request) {
        try {
            Car car = service.getCarById(id);
            if(car == null) {
                return ResponseEntity.notFound().build();
            }
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads/" + filename);

            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" +
                    request.getServerPort();
            String imageUrl = baseUrl + "/api/cars/images" + filename;

            car.setImageUrl(imageUrl);
            service.addCar(car);

            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws MalformedURLException {
        Path file = Paths.get("uploads/").resolve(filename);

        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(resource);
    }
    
    @GetMapping
    public Page<Car> getAllCars(Pageable pageable) {
        return service.getAllCars(pageable);
    }

    @GetMapping("/search")
    public Page<Car> searchCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable) {
        return service.searchCars(make, model, minPrice, maxPrice, pageable);
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
        return service.getCarById(id);
    }

    @PostMapping
    public Car addCar(@RequestBody Car car) {
        return service.addCar(car);
    }

    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @RequestBody Car car) {
        return service.updateCar(id, car);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        service.deleteCar(id);
    }
}
