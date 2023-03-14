package com.w2m.controller;

public class SuperheroDTO {

    public SuperheroDTO() {

    }

    public SuperheroDTO(String name, String superpower, long strength) {
        this(null, name, superpower, strength);
    }

    public SuperheroDTO(Long id, String name, String superpower, long strength) {
        this.id = id;
        this.name = name;
        this.superpower = superpower;
        this.strength = strength;
    }

    private Long id;
    private String name;

    private String superpower;

    private long strength;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperpower() {
        return superpower;
    }

    public void setSuperpower(String superpower) {
        this.superpower = superpower;
    }

    public long getStrength() {
        return strength;
    }

    public void setStrength(long strength) {
        this.strength = strength;
    }

    public String toString() {
        return String.format("Superhero{Id: %d, Name: %s, Superpower: %s, Strength :%d}",
                this.id, this.name, this.superpower, this.strength);
    }
}
