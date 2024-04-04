package org.example.location.repository;

import org.example.location.model.Geodata;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface GeodataRepository extends CrudRepository<Geodata, Integer> {
    Optional<Geodata> findByName(String name);
}