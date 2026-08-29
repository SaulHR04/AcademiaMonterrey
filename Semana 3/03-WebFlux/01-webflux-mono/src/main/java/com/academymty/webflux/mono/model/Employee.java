package com.academymty.webflux.mono.model;

/**
 * El mismo Employee de los proyectos 15 y 16, como record.
 * Un record es inmutable: encaja con el estilo funcional del que habla el mapa mental.
 */
public record Employee(int id, String firstName, String lastName, String email) {
}
