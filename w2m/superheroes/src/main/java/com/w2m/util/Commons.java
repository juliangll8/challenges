package com.w2m.util;

import com.w2m.persistence.Superhero;
import com.w2m.controller.SuperheroDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import static org.modelmapper.internal.util.Objects.firstNonNull;

public class Commons {

    private static ModelMapper modelMapper = new ModelMapper();

    public static Superhero heroDTOtoEntity(SuperheroDTO dto) {
        return modelMapper.map(dto, Superhero.class);
    }

    public static SuperheroDTO heroEntityToDTO(Superhero entity) {
        return modelMapper.map(entity, SuperheroDTO.class);
    }

    public static void updateEntityWithDTO(Superhero entity, SuperheroDTO superheroDTO) {
        entity.setName(superheroDTO.getName());
        entity.setSuperpower(superheroDTO.getSuperpower());
        entity.setStrength(superheroDTO.getStrength());
    }

    private static Map<Class, HttpStatus> exceptionToHttpCode = Map.of(
            NoSuchElementException.class, HttpStatus.NOT_FOUND,
            IllegalArgumentException.class, HttpStatus.BAD_REQUEST
    );

    public static ResponseEntity runAndGenerateResponse(Callable<ResponseEntity> f) {
        try {
            return f.call();
        } catch (Exception e) {
            HttpStatus returnStatus = firstNonNull(
                    exceptionToHttpCode.get(e.getClass()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
            return new ResponseEntity(e.getMessage(), returnStatus);
        }
    }

    public static <E> ResponseEntity<E> getResponseEntity(E toRespond, HttpStatus created) {
        return new ResponseEntity<E>(toRespond, new HttpHeaders(), created);
    }

    public static void validateOrThrowException(boolean result, String msg) {
        if (!result) {
            throw new IllegalArgumentException(msg);
        }
    }
}
