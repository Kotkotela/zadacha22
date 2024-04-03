package org.example.location.controller;

import org.example.location.model.Geodata;
import org.example.location.model.Weather;
import org.example.location.repository.GeodataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private GeodataRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/weather")
    public ResponseEntity<Weather> getWeatherFromLocation(@RequestParam String location) {
        Geodata geodata = repository.findByName(location)
                .orElseThrow(() -> new NoSuchElementException("Location not found: " + location));

        String weatherServiceUrl = "http://localhost:8082/weather/lat={lat}&lon={lon}";
        Weather weather = restTemplate.getForObject(weatherServiceUrl, Weather.class, geodata.getLat(), geodata.getLon());

        if (weather != null) {
            return ResponseEntity.ok(weather);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping
    public Optional<Geodata> getWeather(@RequestParam String location) {
        return repository.findByName(location);
    }

    @PostMapping
    public Geodata save(@RequestBody Geodata geodata) {
        return repository.save(geodata);
    }
}