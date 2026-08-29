package com.academymty.webflux.mono.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Te dice que hilo te esta atendiendo.
 *
 * Llamalo diez veces seguidas: vas a ver que se repiten unos pocos nombres
 * (reactor-http-nio-1, -2, -3, -4...), no diez distintos. Ese es el event loop.
 * En el proyecto 15, con Tomcat, verias diez nombres diferentes.
 */
@RestController
public class HiloRestController {

    /**
     * OJO, AQUI EL ORDEN DE LOS CAMPOS IMPORTA. Esto era un Map.of(...) y el
     * JSON salia con los campos barajados: unas veces "hilo" primero, otras
     * "pista". No era un bug de Spring ni de Jackson.
     *
     * Map.of() NO garantiza orden de iteracion, y Java va mas alla: lo
     * ALEATORIZA EN CADA ARRANQUE de la JVM (ImmutableCollections.SALT, sembrado
     * con System.nanoTime()), justo para que nadie escriba codigo que dependa de
     * un orden que nunca se prometio. Compruebalo tu mismo si quieres:
     *
     *   echo 'System.out.println(java.util.Map.of("a",1,"b",2,"c",3).keySet()); /exit' > /tmp/orden.jsh
     *   for i in 1 2 3 4 5; do jshell -q /tmp/orden.jsh | grep '^\['; done
     *
     * Cinco arranques, cinco ordenes distintos.
     *
     * Con un record el orden es el de declaracion, siempre. Por eso la guia
     * puede hacer `cut -d, -f1` y contar con que sale "hilo".
     */
    public record Hilo(String hilo, int hilosDisponibles, String pista) {}

    @GetMapping("/api/hilo")
    public Mono<Hilo> hilo() {
        return Mono.just(new Hilo(
                Thread.currentThread().getName(),
                Runtime.getRuntime().availableProcessors(),
                "Llama varias veces: se repiten los mismos nombres. Eso es el event loop."
        ));
    }
}
