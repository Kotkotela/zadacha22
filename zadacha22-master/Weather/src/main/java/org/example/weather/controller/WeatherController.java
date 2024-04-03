package org.example.weather.controller;


import org.example.weather.model.Main;
import org.example.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @GetMapping("/lat={lat}&lon={lon}")
    public ResponseEntity<Main> getWeather(@PathVariable Double lat, @PathVariable Double lon) {
        Main weather = weatherService.getWeather(lat, lon);

        if (weather != null) {
            return ResponseEntity.ok(weather);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}




