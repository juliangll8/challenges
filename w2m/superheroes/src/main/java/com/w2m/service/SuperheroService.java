package com.w2m.service;

import com.w2m.controller.SuperheroController;
import com.w2m.persistence.Superhero;
import com.w2m.controller.SuperheroDTO;
import com.w2m.persistence.SuperheroRepository;
import com.w2m.util.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.w2m.util.Commons.*;
import static org.modelmapper.internal.util.Objects.firstNonNull;

@Service
public class SuperheroService {

    Logger logger = Logger.getLogger(SuperheroController.class.getName());

    @Autowired
    SuperheroRepository superheroRepository;

    @Transactional
    public SuperheroDTO createSuperhero(SuperheroDTO superheroDTO) {
        logger.info(String.format("Saving Superhero %s", superheroDTO.getName()));
        validateOrThrowException(superheroDTO.getId() == null, "Id must be empty for creation.");
        validateOrThrowException(superheroDTO.getName() != null, "Name must be set.");
        validateOrThrowException(!superheroRepository.existsByNameLowercased(superheroDTO.getName().toLowerCase()),
                "Superhero already existed. If has been deleted, you need to resurrect it first.");
        Superhero storedSuperhero = superheroRepository.saveAndFlush(heroDTOtoEntity(superheroDTO));
        return heroEntityToDTO(storedSuperhero);
    }

    @Transactional
    public void deleteSuperhero(Long id) {
        logger.info(String.format("Deleting Superhero %d", id));
        Optional<Superhero> deletedSuperhero = superheroRepository.findById(id);
        deletedSuperhero.ifPresent(hero -> hero.setActive(false));
    }

    @Transactional
    public void resurrectSuperhero(String name) {
        validateOrThrowException(name != null, "Name must be set.");
        logger.info(String.format("Resurrecting Superhero %s", name));
        Optional<Superhero> resurrectingSuperhero = superheroRepository.findByNameIgnoreCase(name);
        throwNoSuchElementIfEmpty(resurrectingSuperhero);
        resurrectingSuperhero.get().setActive(true);
    }

    @Transactional
    public SuperheroDTO updateSuperhero(Long id, SuperheroDTO superheroDTO) {
        validateOrThrowException(id != null, "Id must be set for updates.");
        validateOrThrowException(id.equals(superheroDTO.getId()), "Ids must be the same ones.");
        logger.info(String.format("Updating Superhero %s", superheroDTO.getName()));
        Optional<Superhero> storedSuperhero = superheroRepository.findByIdAndActiveTrue(id);
        throwNoSuchElementIfEmpty(storedSuperhero);
        Superhero superhero = storedSuperhero.get();
        validateOrThrowException(superhero.getName().equals(superheroDTO.getName()), "Superhero name must remain the same.");
        updateEntityWithDTO(superhero, superheroDTO);
        return heroEntityToDTO(superhero);
    }

    public List<SuperheroDTO> returnAllSuperheroes() {
        return returnSuperheroesMatchingName("");
    }

    public List<SuperheroDTO> returnSuperheroesMatchingName(String search) {
        logger.info("Fetching all Superheroes");
        String searchWithWildcard = "%" + firstNonNull(search, "") + "%";
        List<Superhero> superheroes = superheroRepository.findByActiveTrueAndNameLikeIgnoreCase(searchWithWildcard);
        return superheroes.stream().map(Commons::heroEntityToDTO).collect(Collectors.toList());
    }

    private static void throwNoSuchElementIfEmpty(Optional<Superhero> storedSuperhero) {
        if (storedSuperhero.isEmpty()) {
            throw new NoSuchElementException("Superhero doesn't exist.");
        }
    }
}
