package com.academymty.webflux.mono.repo;

import com.academymty.webflux.mono.model.Employee;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Un "repositorio" de mentira: los datos viven en memoria.
 *
 * No hay base de datos a proposito. Lo que se aprende aqui es Mono, y cada minuto
 * peleando con Docker es un minuto que no se dedica a eso.
 *
 * El delayElement(5 s) simula lo unico que nos interesa de una base de datos real:
 * QUE TARDA. Sin esa espera, todo este tema no tendria sentido.
 */
@Repository
public class EmployeeRepository {

    /** Lo que tarda "la base de datos" en contestar. */
    public static final Duration LATENCIA = Duration.ofMillis(5000);   // 5 segundos

    private final Map<Integer, Employee> tabla = new ConcurrentHashMap<>(Map.of(
            1, new Employee(1, "Leslie",  "Andrews", "leslie@luv2code.com"),
            2, new Employee(2, "Emma",    "Baumgarten", "emma@luv2code.com"),
            3, new Employee(3, "Avani",   "Gupta",   "avani@luv2code.com"),
            4, new Employee(4, "Yuri",    "Petrov",  "yuri@luv2code.com"),
            5, new Employee(5, "Juan",    "Ramirez", "juan@academymty.mx")
    ));

    /**
     * Version reactiva: devuelve la RECETA de como conseguir el empleado.
     *
     * Ojo con lo que NO pasa aqui: al llamar a este metodo no se busca nada y no se
     * espera nada. Se devuelve un Mono y el metodo termina de inmediato. La busqueda
     * ocurre cuando alguien se suscribe -- y en un @RestController, quien se suscribe
     * es Spring, no tu.
     */
    public Mono<Employee> findById(int id) {
        return Mono.justOrEmpty(tabla.get(id))   // justOrEmpty: si no esta, Mono vacio
                   .delayElement(LATENCIA);      // "la base de datos tarda"
    }

    /**
     * Version bloqueante: la misma consulta, pero el hilo se queda parado esperando.
     *
     * Esto es el proyecto 15. Existe aqui SOLO para poder medir la diferencia
     * en scripts/comparar.sh. En una app WebFlux de verdad, esto es un bug.
     */
    public Employee findByIdBloqueante(int id) {
        try {
            Thread.sleep(LATENCIA.toMillis());   // el hilo se duerme, y no puede hacer nada mas
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return tabla.get(id);
    }
}
