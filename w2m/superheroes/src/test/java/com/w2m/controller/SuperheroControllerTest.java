package com.w2m.controller;

import com.w2m.service.SuperheroService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SuperheroControllerTest {

    @Mock
    private SuperheroService superheroService;

    @InjectMocks
    private SuperheroController superheroController;

    @Test
    public void createSuperheroSuccessfully() {
        SuperheroDTO storedSuperhero = new SuperheroDTO("name", "power", 10L);
        when(superheroService.createSuperhero(any())).thenReturn(storedSuperhero);
        ResponseEntity<SuperheroDTO> createResponse = superheroController.createSuperhero(storedSuperhero);
        assertCodeAndBodyResponse(createResponse, HttpStatus.CREATED, storedSuperhero);
    }

    @Test
    public void deleteSuperheroSuccessfully() {
        ResponseEntity<String> deleteResponse = superheroController.deleteSuperhero(123L);
        assertCodeAndBodyResponse(deleteResponse, HttpStatus.NO_CONTENT, "123 was removed.");
    }

    @Test
    public void resurrectSuperheroSuccessfully() {
        ResponseEntity<String> resurrectResponse = superheroController.resurrectSuperhero("HeroResurrected");
        assertCodeAndBodyResponse(resurrectResponse, HttpStatus.OK, "HeroResurrected was resurrected.");
    }

    @Test
    public void updateSuperheroSuccessfully() {
        SuperheroDTO storedSuperhero = new SuperheroDTO(2L, "name", "power", 10L);
        when(superheroService.updateSuperhero(any(), any())).thenReturn(storedSuperhero);
        ResponseEntity<SuperheroDTO> updateResponse = superheroController.updateSuperhero(storedSuperhero, 2L);
        assertCodeAndBodyResponse(updateResponse, HttpStatus.OK, storedSuperhero);
    }

    @Test
    public void fetchSuperheroSuccessfully() {
        List<SuperheroDTO> superheroDTOList = asList(new SuperheroDTO(1L, "name1", "power1", 10L),
                new SuperheroDTO(2L, "name2", "power2", 20L));
        when(superheroService.returnSuperheroesMatchingName(any())).thenReturn(superheroDTOList);
        ResponseEntity<List<SuperheroDTO>> getResponse = superheroController.getSuperheroes(null);
        assertCodeAndBodyResponse(getResponse, HttpStatus.OK, superheroDTOList);
    }

    @ParameterizedTest
    @MethodSource("pairExceptionHttpCode")
    public void validateExceptionMatchProperHttpCodeTest(Class exceptionClass, HttpStatus httpCodeResponse) {
        when(superheroService.createSuperhero(any())).thenThrow(exceptionClass);
        ResponseEntity<SuperheroDTO> exceptionResponse = superheroController.createSuperhero(null);
        assertCodeAndBodyResponse(exceptionResponse, httpCodeResponse, null);
    }

    private static Stream<Arguments> pairExceptionHttpCode() {
        return Stream.of(
                Arguments.of(IllegalArgumentException.class, HttpStatus.BAD_REQUEST),
                Arguments.of(NoSuchElementException.class, HttpStatus.NOT_FOUND),
                Arguments.of(RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    private static <E> void assertCodeAndBodyResponse(ResponseEntity<E> response, HttpStatus httpCode, E body) {
        assertThat(response)
                .extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
                .containsExactly(httpCode, body);
    }
}
