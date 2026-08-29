package com.academymty.webflux.mono;

import com.academymty.webflux.mono.model.Employee;
import com.academymty.webflux.mono.repo.EmployeeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Aqui es donde de verdad se entiende un Mono.
 *
 * En el controlador no se nota nada, porque Spring se suscribe por ti.
 * En un test no hay nadie que lo haga: si tu no te suscribes, no pasa nada.
 */
class EmployeeRepositoryTest {

    /**
     * Sin esto, un StepVerifier que espera una senal que nunca llega se queda
     * colgado PARA SIEMPRE en vez de fallar, y revienta el build de CI.
     * Lo descubrimos rompiendo el codigo a proposito: la suite se colgo 8 minutos.
     *
     * El margen se DERIVA de LATENCIA en vez de ser un 10 fijo. Con los 300 ms
     * originales, un timeout de 10 s daba 33x de holgura; al subir la latencia a
     * 5 s ese mismo 10 se quedo en 2x, a un mal dia de CI de volverse intermitente.
     * Atado a la constante, no puede volver a desfasarse.
     */
    @BeforeAll
    static void noColgarseNunca() {
        StepVerifier.setDefaultTimeout(EmployeeRepository.LATENCIA.plusSeconds(10));
    }


    private final EmployeeRepository repo = new EmployeeRepository();

    @Test
    @DisplayName("Sin subscribe() no se ejecuta NADA")
    void sinSubscribeNoPasaNada() {
        AtomicBoolean seEjecuto = new AtomicBoolean(false);

        Mono<String> receta = Mono.fromCallable(() -> {
            seEjecuto.set(true);            // esto solo corre si alguien se suscribe
            return "cocinado";
        });

        // Ya construimos el Mono... y sin embargo:
        assertFalse(seEjecuto.get(),
                "El Mono es la RECETA, no el plato: todavia no ha corrido nada");

        receta.block();                     // block() se suscribe y espera (solo en tests!)

        assertTrue(seEjecuto.get(), "Ahora si: alguien se suscribio");
    }

    @Test
    @DisplayName("Un empleado que existe emite un valor y luego onComplete")
    void empleadoQueExiste() {
        StepVerifier.create(repo.findById(1))
                .assertNext(e -> assertEquals("Leslie", e.firstName()))   // onNext
                .verifyComplete();                                        // onComplete
    }

    @Test
    @DisplayName("El dato TARDA: sin latencia, este proyecto no demuestra nada")
    void tardaLoQueDebeTardar() {
        // Este test existe por un motivo concreto: la latencia simulada ES la leccion.
        // Si alguien borra el .delayElement(), el experimento de scripts/comparar.sh
        // deja de mostrar diferencia y el proyecto pierde el sentido -- pero todos los
        // demas tests seguirian en verde. Este lo caza.
        //
        // expectNoEvent(d) verifica que durante ese tiempo NO llega ninguna senal.
        StepVerifier.withVirtualTime(() -> repo.findById(1))
                .expectSubscription()
                .expectNoEvent(EmployeeRepository.LATENCIA)   // 5 segundos de silencio
                .assertNext(e -> assertEquals("Leslie", e.firstName()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Un empleado que no existe NO es un error: es un flujo vacio")
    void empleadoQueNoExiste() {
        StepVerifier.create(repo.findById(999))
                .verifyComplete();          // cero onNext, y directo a onComplete
    }

    @Test
    @DisplayName("El error viaja por el flujo, no por la pila")
    void elErrorEsUnaSenal() {
        Mono<Employee> roto = repo.findById(1)
                .flatMap(e -> Mono.error(new IllegalStateException("truena")));

        StepVerifier.create(roto)
                .verifyError(IllegalStateException.class);   // onError, no una excepcion lanzada
    }

    @Test
    @DisplayName("onErrorResume sustituye el error por un valor de repuesto")
    void planB() {
        Mono<Employee> conRepuesto = repo.findById(1)
                .flatMap(e -> Mono.<Employee>error(new IllegalStateException("truena")))
                .onErrorResume(ex -> Mono.just(new Employee(-1, "Plan", "B", "b@academymty.mx")));

        StepVerifier.create(conRepuesto)
                .assertNext(e -> assertEquals("Plan", e.firstName()))
                .verifyComplete();
    }
}
