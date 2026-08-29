package com.academymty.webflux.flux;

import com.academymty.webflux.flux.model.Lectura;
import com.academymty.webflux.flux.service.SensorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testear un flujo que tarda 1 segundo por elemento seria eterno.
 * StepVerifier.withVirtualTime() adelanta el reloj: 20 segundos de flujo
 * se verifican en milisegundos.
 */
class SensorServiceTest {

    /**
     * Sin esto, un StepVerifier que espera una senal que nunca llega se queda
     * colgado PARA SIEMPRE en vez de fallar, y revienta el build de CI.
     * Lo descubrimos rompiendo el codigo a proposito: la suite se colgo 8 minutos.
     */
    @BeforeAll
    static void noColgarseNunca() {
        StepVerifier.setDefaultTimeout(Duration.ofSeconds(10));
    }


    private final SensorService sensor = new SensorService();

    @Test
    @DisplayName("Emite una lectura por segundo, en orden")
    void emiteEnOrden() {
        StepVerifier.withVirtualTime(() -> sensor.lecturas().take(3))
                .thenAwait(Duration.ofSeconds(3))
                .assertNext(l -> assertEquals(0, l.numero()))
                .assertNext(l -> assertEquals(1, l.numero()))
                .assertNext(l -> assertEquals(2, l.numero()))
                .verifyComplete();
    }

    @Test
    @DisplayName("La temperatura se mueve entre 18 y 34 grados")
    void rangoDeTemperatura() {
        StepVerifier.withVirtualTime(() -> sensor.lecturas().take(20))
                .thenAwait(Duration.ofSeconds(20))
                .thenConsumeWhile(l -> l.celsius() >= 18 && l.celsius() <= 34)
                .verifyComplete();
    }

    @Test
    @DisplayName("filter() deja pasar solo lo que supera el umbral")
    void elFiltroFiltra() {
        // OJO CON ESTO. El Flux se construye DENTRO del lambda, no fuera.
        //
        // withVirtualTime() instala el reloj falso justo antes de ejecutar este
        // lambda. Si construyes el Flux antes -- por ejemplo asi:
        //
        //     Flux<Lectura> calientes = sensor.lecturas().take(20).filter(...);
        //     StepVerifier.withVirtualTime(() -> calientes)     // <-- MAL
        //
        // ...entonces Flux.interval() ya se quedo con el scheduler DE VERDAD en el
        // momento de montarse, y el test tarda 20 segundos reales en vez de 0,2.
        // Lo comprobamos: esa version tardaba 20,23 s; esta tarda 0,19 s.
        StepVerifier.withVirtualTime(() ->
                        sensor.lecturas().take(20).filter(l -> l.celsius() > 30))
                .thenAwait(Duration.ofSeconds(20))
                .thenConsumeWhile(l -> l.celsius() > 30)
                .verifyComplete();
    }

    @Test
    @DisplayName("Un Flux se colapsa en un Mono con collectList()")
    void deFluxAMono() {
        StepVerifier.withVirtualTime(() -> sensor.lecturas().take(5).collectList())
                .thenAwait(Duration.ofSeconds(5))
                .assertNext(lista -> assertEquals(5, lista.size()))   // UN solo valor: una lista
                .verifyComplete();
    }

    @Test
    @DisplayName("El flujo crudo es INFINITO: nunca manda onComplete")
    void esInfinito() {
        StepVerifier.withVirtualTime(sensor::lecturas)
                .thenAwait(Duration.ofSeconds(30))
                .expectNextCount(30)
                .thenCancel()          // hay que cancelarlo: solo no se para nunca
                .verify();
    }
}
