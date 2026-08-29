package com.academymty.webflux.flux.model;

/**
 * Una lectura del sensor de temperatura.
 *
 * Este dominio no es casualidad: el mapa mental lista "agregar informacion de
 * sensores de temperatura para controlar la calefaccion" como caso de uso reactivo.
 * Un empleado que llega cada segundo seria artificial; un sensor no.
 */
public record Lectura(long numero, String sensor, double celsius, String hora) {
}
