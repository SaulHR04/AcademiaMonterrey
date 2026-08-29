package com.academymty.webflux.flux.service;

import com.academymty.webflux.flux.model.Lectura;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * El sensor. Aqui esta TODO el proyecto en cuatro lineas.
 *
 * Flux.interval(1s) emite 0, 1, 2, 3... para siempre, uno por segundo.
 * Es un flujo INFINITO: nunca manda onComplete. Eso es algo que una List<T>
 * no puede representar ni de broma, y es la razon de que Flux exista.
 */
@Service
public class SensorService {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    /** Cada cuanto llega una lectura nueva. */
    public static final Duration CADENCIA = Duration.ofSeconds(1);

    /**
     * El flujo crudo: una lectura por segundo, sin fin.
     *
     * La temperatura sigue una onda: sube y baja entre ~18 y ~34 grados en ciclos
     * de 20 segundos. Es a proposito, no aleatorio: asi las alertas se disparan de
     * forma predecible y puedes comprobar que el filtro hace lo que dice.
     */
    public Flux<Lectura> lecturas() {
        return Flux.interval(CADENCIA)
                   .map(this::medir);
    }

    private Lectura medir(long n) {
        double celsius = 26 + 8 * Math.sin(2 * Math.PI * n / 20.0);
        return new Lectura(
                n,
                "sensor-A",
                Math.round(celsius * 10) / 10.0,
                LocalTime.now().format(HORA));
    }
}
