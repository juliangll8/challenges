package com.w2m.util;

import com.w2m.persistence.Superhero;
import com.w2m.controller.SuperheroDTO;
import org.junit.jupiter.api.Test;

import static com.w2m.util.Commons.heroDTOtoEntity;
import static com.w2m.util.Commons.heroEntityToDTO;
import static org.assertj.core.api.Assertions.assertThat;

public class ModelMapperTest {

    @Test
    public void convertDTOtoEntity() {
        SuperheroDTO shDTO = new SuperheroDTO(1L, "name", "power", 10L);
        Superhero shEntity = heroDTOtoEntity(shDTO);
        assertThat(shDTO.getName()).isEqualTo(shEntity.getName());
        assertThat(shDTO.getSuperpower()).isEqualTo(shEntity.getSuperpower());
        assertThat(shDTO.getStrength()).isEqualTo(shEntity.getStrength());
        assertThat(shEntity.getId()).isEqualTo(1L);
        assertThat(shEntity.isActive()).isTrue();
    }

    @Test
    public void convertEntityToDTO() {
        Superhero shEntity = new Superhero("name", "power", 10L);
        shEntity.setActive(false);
        SuperheroDTO shDTO = heroEntityToDTO(shEntity);
        assertThat(shEntity.getName()).isEqualTo(shDTO.getName());
        assertThat(shEntity.getSuperpower()).isEqualTo(shDTO.getSuperpower());
        assertThat(shEntity.getStrength()).isEqualTo(shDTO.getStrength());
        assertThat(shEntity.getId()).isNull();
        assertThat(shEntity.isActive()).isFalse();
    }
}
