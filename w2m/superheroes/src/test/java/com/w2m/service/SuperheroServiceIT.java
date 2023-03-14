package com.w2m.service;

import com.w2m.persistence.Superhero;
import com.w2m.controller.SuperheroDTO;
import com.w2m.persistence.SuperheroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.w2m.util.Commons.heroEntityToDTO;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class SuperheroServiceIT {

    @Autowired
    SuperheroRepository superheroRepository;

    @Autowired
    SuperheroService superheroService;

    private final Superhero sh1Entity = new Superhero("Hero1", "Superpower1", 10L);

    private final SuperheroDTO sh1DTO = heroEntityToDTO(sh1Entity);

    private final Superhero sh2Entity = new Superhero("Hero2", "Superpower2", 20L);

    @BeforeEach
    public void init() {
        superheroRepository.deleteAll();
    }

    @Test
    public void shouldCreateSuperheroTest() {
        assertThat(superheroRepository.count()).isZero();
        String name = "Messi";
        String superpower = "Soccer";
        long strength = 100000L;
        superheroService.createSuperhero(new SuperheroDTO(name, superpower, strength));

        assertThat(superheroRepository.findAll())
                .hasSize(1)
                .first().extracting( "name", "superpower", "strength", "active")
                .contains(name, superpower, strength, true);
    }

    @Test
    public void shouldReturnSuperheroesList() {
        assertThat(superheroService.returnAllSuperheroes()).hasSize(0);
        superheroRepository.saveAndFlush(sh1Entity);
        assertThat(superheroService.returnAllSuperheroes()).hasSize(1);
        superheroRepository.saveAndFlush(sh2Entity);
        List<SuperheroDTO> superheroes = superheroService.returnAllSuperheroes();
        assertThat(superheroes)
                .hasSize(2)
                .extracting("id", "name", "superpower", "strength")
                .contains(
                        tuple(sh2Entity.getId(), sh2Entity.getName(), sh2Entity.getSuperpower(), sh2Entity.getStrength()),
                        tuple(sh1Entity.getId(), sh1Entity.getName(), sh1Entity.getSuperpower(), sh1Entity.getStrength())
                );
    }

    @Test
    public void shouldDeleteByIdAndSeveralTimes() {
        SuperheroDTO createdSuperhero = superheroService.createSuperhero(sh1DTO);
        assertThat(superheroService.returnAllSuperheroes()).hasSize(1);

        superheroService.deleteSuperhero(createdSuperhero.getId());
        assertThat(superheroService.returnAllSuperheroes()).hasSize(0);
        superheroService.deleteSuperhero(createdSuperhero.getId());
        assertThat(superheroRepository.count()).isOne();
    }

    @Test
    public void shouldFailToCreateIfNameExists() {
        superheroService.createSuperhero(sh1DTO);
        assertThatThrownBy(() -> superheroService.createSuperhero(sh1DTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Superhero already existed. If has been deleted, you need to resurrect it first.");
        assertThat(superheroService.returnAllSuperheroes()).hasSize(1);
    }

    @Test
    public void shouldFailIfHeroWasDeleted() {
        Superhero initialSH = new Superhero("RepeteadName", "Superpower", 20L);
        Superhero SimilarNameSH = new Superhero("RePeTeaDnAmE", "AnotherSuperPower", 20L);
        SuperheroDTO createdSuperhero = superheroService.createSuperhero(heroEntityToDTO(initialSH));
        superheroService.deleteSuperhero(createdSuperhero.getId());
        assertThat(superheroService.returnAllSuperheroes()).hasSize(0);

        assertThatThrownBy(() -> superheroService.createSuperhero(heroEntityToDTO(SimilarNameSH)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Superhero already existed. If has been deleted, you need to resurrect it first.");
    }

    @Test
    public void shouldResurrect() {
        SuperheroDTO createdSuperhero = superheroService.createSuperhero(sh1DTO);
        assertThat(superheroService.returnAllSuperheroes()).hasSize(1);
        superheroService.deleteSuperhero(createdSuperhero.getId());
        assertThat(superheroService.returnAllSuperheroes()).hasSize(0);
        superheroService.resurrectSuperhero(sh1Entity.getName());
        assertThat(superheroService.returnAllSuperheroes()).hasSize(1);
    }

    @Test
    public void shouldEditHero() {
        String name = "SomeName";
        String initialSuperpower = "SomeSuperpower";
        long initialStrength = 2000L;
        SuperheroDTO toEditSH = superheroService.createSuperhero(new SuperheroDTO(name, initialSuperpower, initialStrength));
        assertThat(superheroService.returnAllSuperheroes())
                .hasSize(1)
                .extracting("id", "name", "superpower", "strength")
                .contains(
                        tuple(toEditSH.getId(), name, initialSuperpower, initialStrength)
                );
        String editedSuperpower = "AnotherPower";
        long editedStrength = 1000L;
        toEditSH.setSuperpower(editedSuperpower);
        toEditSH.setStrength(editedStrength);

        SuperheroDTO editedSH = superheroService.updateSuperhero(toEditSH.getId(), toEditSH);
        assertThat(superheroService.returnAllSuperheroes())
                .hasSize(1)
                .extracting("id", "name", "superpower", "strength")
                .contains(
                        tuple(editedSH.getId(), name, editedSuperpower, editedStrength)
                );

        assertThat(toEditSH.getId()).isEqualTo(editedSH.getId());
        assertThat(toEditSH.getName()).isEqualTo(editedSH.getName());
    }

    @Test
    public void shouldSearchByNameCaseInsensitive() {
        String superpower = "SomeSuperpower";
        long strength = 2000L;
        superheroService.createSuperhero(new SuperheroDTO("Superman", superpower, strength));
        superheroService.createSuperhero(new SuperheroDTO("Spiderman", superpower, strength));
        superheroService.createSuperhero(new SuperheroDTO("Batman", superpower, strength));
        assertThat(superheroService.returnAllSuperheroes()).hasSize(3);

        assertThat(superheroService.returnSuperheroesMatchingName("SuPeRMaN")).hasSize(1);
        assertThat(superheroService.returnSuperheroesMatchingName("erman")).hasSize(2);
        assertThat(superheroService.returnSuperheroesMatchingName("MAN")).hasSize(3);
    }
}
