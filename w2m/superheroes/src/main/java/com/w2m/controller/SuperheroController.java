package com.w2m.controller;


import com.w2m.service.SuperheroService;
import com.w2m.util.LogExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import static com.w2m.util.Commons.*;

@RestController
@RequestMapping("/api/superhero")
public class SuperheroController {

    Logger logger = Logger.getLogger(SuperheroController.class.getName());
    @Autowired
    private SuperheroService superheroService;

    @Autowired
    private CacheManager cacheManager;

    @LogExecutionTime
    @CacheEvict(value="Superheroes", allEntries=true)
    @PostMapping
    public ResponseEntity<SuperheroDTO> createSuperhero(@RequestBody SuperheroDTO superhero) {
        logger.info("Create Superhero: " + superhero);
        Callable<ResponseEntity> f = () -> {
            SuperheroDTO createdSuperhero = superheroService.createSuperhero(superhero);
            logger.info("Created " + createdSuperhero);
            return getResponseEntity(createdSuperhero, HttpStatus.CREATED);
        };
        return runAndGenerateResponse(f);
    }

    @LogExecutionTime
    @CacheEvict(value="Superheroes", allEntries=true)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSuperhero(@PathVariable Long id) {
        logger.info("Deleting Superhero: " + id);
        Callable<ResponseEntity> f = () -> {
            superheroService.deleteSuperhero(id);
            String msg = id + " was removed.";
            logger.info(msg);
            return getResponseEntity(msg, HttpStatus.NO_CONTENT);
        };
        return runAndGenerateResponse(f);
    }

    @LogExecutionTime
    @CacheEvict(value="Superheroes", allEntries=true)
    @PostMapping("/resurrect")
    public ResponseEntity<String> resurrectSuperhero(@RequestBody String name) {
        logger.info("Resurrecting Superhero: " + name);
        Callable<ResponseEntity> f = () -> {
            superheroService.resurrectSuperhero(name);
            String msg = name + " was resurrected.";
            logger.info(msg);
            return getResponseEntity(msg, HttpStatus.OK);
        };
        return runAndGenerateResponse(f);
    }

    @LogExecutionTime
    @CacheEvict(value="Superheroes", allEntries=true)
    @PutMapping("/{id}")
    public ResponseEntity<SuperheroDTO> updateSuperhero(@RequestBody SuperheroDTO superhero, @PathVariable Long id) {
        logger.info("Update Superhero: " + superhero.getName());
        Callable<ResponseEntity> f = () -> {
            SuperheroDTO updatedSuperhero = superheroService.updateSuperhero(id, superhero);
            logger.info("Updated " + updatedSuperhero);
            return getResponseEntity(updatedSuperhero, HttpStatus.OK);
        };
        return runAndGenerateResponse(f);
    }

    @LogExecutionTime
    @Cacheable("Superheroes")
    @GetMapping("/all")
    public ResponseEntity<List<SuperheroDTO>> getSuperheroes(@RequestBody(required = false) String matchingName) {
        logger.info("Return all Superheroes");
        Callable<ResponseEntity> f = () -> {
            List<SuperheroDTO> superheroDTOS = superheroService.returnSuperheroesMatchingName(matchingName);
            logger.info(String.format("Fetched %d superheroes.", superheroDTOS.size()));
            return getResponseEntity(superheroDTOS, HttpStatus.OK);
        };
        return runAndGenerateResponse(f);
    }
}
