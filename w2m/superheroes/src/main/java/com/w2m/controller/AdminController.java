package com.w2m.controller;


import com.w2m.service.SuperheroService;
import com.w2m.util.LogExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import static com.w2m.util.Commons.getResponseEntity;
import static com.w2m.util.Commons.runAndGenerateResponse;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    Logger logger = Logger.getLogger(AdminController.class.getName());
    @Autowired
    private SuperheroService superheroService;

    private static boolean alreadyExecuted = false;

    @LogExecutionTime
    @CacheEvict(value="Superheroes", allEntries=true)
    @PostMapping("/initialSetup")
    public ResponseEntity<String> initialSetup() {
        if (alreadyExecuted) {
            logger.info("Setup already executed, can be done only once");
            return getResponseEntity("Initial setup was already executed.", HttpStatus.OK);
        }

        alreadyExecuted = true;
        logger.info("Create few Superheroes");
        Callable<ResponseEntity> f = () -> {
            SuperheroDTO superman = createSuperhero("Superman", "Superpowers", 2000L);
            SuperheroDTO spiderman = createSuperhero("Spider-man", "Spider powers", 1000);
            SuperheroDTO batman = createSuperhero("Batman", "Rich", 1500L);
            logger.info(String.format("Created a few superheroes: %s, %s, %s", superman, spiderman, batman));
            return getResponseEntity("Created initial setup successfully.", HttpStatus.OK);
        };
        return runAndGenerateResponse(f);
    }

    private SuperheroDTO createSuperhero(String name, String superpower, long strength) {
        return superheroService.createSuperhero(new SuperheroDTO(name, superpower, strength));
    }
}
