package com.example.person.controller;

import com.example.person.model.Person;
import com.example.person.model.Weather;
import com.example.person.repository.PersonRepository;
import org.example.location.model.Geodata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.stream.Location;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonRepository repository;
    @Autowired
    private RestTemplate restTemplate;


    @GetMapping
    public Iterable<Person> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Optional<Person> person = repository.findById(id);
        return person.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Person newPerson = repository.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPerson);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person personDetails) {
        Optional<Person> optionalPerson = repository.findById(id);
        if (optionalPerson.isPresent()) {
            Person existingPerson = optionalPerson.get();
            existingPerson.setFirstname(personDetails.getFirstname());
            existingPerson.setSurname(personDetails.getSurname());
            existingPerson.setLastname(personDetails.getLastname());
            existingPerson.setBirthday(personDetails.getBirthday());
            existingPerson.setLocation(personDetails.getLocation());
            repository.save(existingPerson);
            return ResponseEntity.ok(existingPerson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/weather")
    public ResponseEntity<Weather> getWeatherForPersonLocation(@PathVariable int id) {
        Optional<Person> personOptional = repository.findById(id);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            Geodata geodata = restTemplate.getForObject("http://location/location?location=" + person.getLocation(), Geodata.class);
            if (geodata != null) {
                String weatherServiceUrl = "http://Weather/weather/lat=" + geodata.getLat() + "&lon=" + geodata.getLon();
                ResponseEntity<Weather> response = restTemplate.getForEntity(weatherServiceUrl, Weather.class);
                return response;
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}



