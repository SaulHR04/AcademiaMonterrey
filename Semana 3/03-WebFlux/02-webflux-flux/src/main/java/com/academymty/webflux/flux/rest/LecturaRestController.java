package com.academymty.webflux.flux.rest;

import com.academymty.webflux.flux.model.Lectura;
import com.academymty.webflux.flux.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/lecturas")
public class LecturaRestController {

    private static final Logger log = LoggerFactory.getLogger(LecturaRestController.class);

    private final SensorService sensor;

    public LecturaRestController(SensorService sensor) {
        this.sensor = sensor;
    }

    // ------------------------------------------------------------------
    //  LOS DOS ENDPOINTS QUE HAY QUE COMPARAR.
    //  Mismo servicio, mismos datos, mismo Flux. Solo cambia el "produces".
    // ------------------------------------------------------------------

    /**
     * (A) JSON normal.
     *
     * Spring se suscribe, JUNTA las 5 lecturas, y cuando el flujo termina manda
     * el array de golpe. El navegador se queda en blanco 5 segundos y de repente
     * aparece todo. Es indistinguible de un List<Lectura> del proyecto 15.
     *
     *   curl http://localhost:8075/api/lecturas
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Lectura> comoJson() {
        log.info("[JSON]  el cliente esperara a que terminen las 5 lecturas");
        return sensor.lecturas().take(5);
    }

    /**
     * (B) El MISMO Flux, servido como stream de eventos.
     *
     * Ahora Spring NO junta nada: cada lectura sale por el cable en cuanto existe.
     * El navegador pinta una linea por segundo. ESTA es la diferencia que un
     * List<T> no puede darte.
     *
     *   curl -N http://localhost:8075/api/lecturas/stream
     *          ^^ la -N es imprescindible: desactiva el buffer de curl
     */
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Lectura> comoStream() {
        log.info("[STREAM] el cliente recibira una lectura por segundo");
        return sensor.lecturas().take(20);
    }

    // ------------------------------------------------------------------
    //  OPERADORES SOBRE UN FLUJO QUE SIGUE CORRIENDO
    // ------------------------------------------------------------------

    /**
     * filter() sobre un flujo vivo: solo las lecturas por encima del umbral.
     *
     * Como la temperatura es una onda de 20 segundos, veras rachas de alertas
     * y luego silencio. El flujo NO se para en los silencios: sigue corriendo,
     * simplemente no emite.
     *
     *   curl -N "http://localhost:8075/api/lecturas/alertas?umbral=30"
     */
    @GetMapping(path = "/alertas", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Lectura> alertas(@RequestParam(defaultValue = "30") double umbral) {
        return sensor.lecturas()
                     .filter(l -> l.celsius() > umbral)
                     .doOnNext(l -> log.warn("ALERTA {} C", l.celsius()));
    }

    /**
     * takeUntil(): el ejemplo literal del mapa mental,
     * "vigila el precio de la accion hasta que baje de 100".
     *
     * Aqui: manda lecturas hasta que la temperatura baje del umbral, y ahi
     * emite onComplete y cierra la conexion sola. Miralo con -N: el curl
     * TERMINA solo, sin Ctrl-C.
     *
     *   curl -N http://localhost:8075/api/lecturas/hasta/20
     */
    @GetMapping(path = "/hasta/{umbral}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Lectura> hasta(@PathVariable double umbral) {
        return sensor.lecturas()
                     .takeUntil(l -> l.celsius() < umbral)
                     .doOnComplete(() -> log.info("bajo de {} C: onComplete y cierro", umbral));
    }

    // ------------------------------------------------------------------
    //  DE VUELTA A MONO: cuando un flujo se colapsa en un solo valor
    // ------------------------------------------------------------------

    /**
     * Un Flux que se convierte en Mono.
     *
     * collectList() espera a que el flujo TERMINE y devuelve todo junto: por eso
     * devuelve Mono<...>, un solo valor. Es la operacion inversa a lo que hace
     * /stream, y explica por que el endpoint (A) parece un List: por dentro,
     * servir un Flux como JSON hace justo esto.
     *
     *   curl http://localhost:8075/api/lecturas/resumen   (tarda 10 s)
     */
    @GetMapping(path = "/resumen", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Resumen> resumen() {
        return sensor.lecturas()
                     .take(10)
                     .collectList()          // Flux<Lectura> -> Mono<List<Lectura>>
                     .map(this::estadisticas);
    }

    /**
     * OJO, AQUI EL ORDEN DE LOS CAMPOS IMPORTA. Esto era un Map.of(...) y el
     * JSON salia con los campos barajados en cada arranque, sin coincidir con
     * el que muestra la guia.
     *
     * Map.of() NO garantiza orden de iteracion, y Java lo ALEATORIZA EN CADA
     * ARRANQUE de la JVM (ImmutableCollections.SALT, sembrado con nanoTime())
     * para que nadie dependa de un orden que nunca se prometio. Con un record
     * el orden es el de declaracion, siempre.
     */
    public record Resumen(int lecturas, double minima, double maxima,
                          double promedio, Lectura masCaliente) {}

    private Resumen estadisticas(List<Lectura> ls) {
        return new Resumen(
                ls.size(),
                ls.stream().mapToDouble(Lectura::celsius).min().orElse(0),
                ls.stream().mapToDouble(Lectura::celsius).max().orElse(0),
                Math.round(ls.stream().mapToDouble(Lectura::celsius).average().orElse(0) * 10) / 10.0,
                ls.stream().max(Comparator.comparingDouble(Lectura::celsius)).orElseThrow()
        );
    }
}
