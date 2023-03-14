package com.w2m.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuperheroRepository extends JpaRepository<Superhero, Long> {

    public boolean existsByNameLowercased(String name);
    public Optional<Superhero> findByIdAndActiveTrue(Long id);
    public Optional<Superhero> findByNameIgnoreCase(String name);
    public List<Superhero> findByActiveTrueAndNameLikeIgnoreCase(String name);
}
