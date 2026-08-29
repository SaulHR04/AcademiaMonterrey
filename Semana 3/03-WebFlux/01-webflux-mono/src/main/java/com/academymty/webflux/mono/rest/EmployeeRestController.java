package com.academymty.webflux.mono.rest;

import com.academymty.webflux.mono.model.Employee;
import com.academymty.webflux.mono.repo.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/**
 * El controlador reactivo. Compara cada metodo con el del proyecto 15:
 * el cuerpo es casi igual, lo que cambia es el TIPO DE RETORNO.
 */
@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeRestController.class);

    private final EmployeeRepository repo;

    public EmployeeRestController(EmployeeRepository repo) {
        this.repo = repo;
    }

    /**
     * Un empleado por id.
     *
     * Proyecto 15:  public Employee       findById(...)  -> el hilo espera aqui
     * Proyecto 01:  public Mono<Employee> findById(...)  -> devuelve el plan y suelta el hilo
     */
    @GetMapping("/employees/{id}")
    public Mono<Employee> findById(@PathVariable int id) {
        log.info("-> pediste el empleado {} (hilo: {})", id, Thread.currentThread().getName());

        return repo.findById(id)
                   .doOnNext(e -> log.info("<- llego {} (hilo: {})",
                           e.firstName(), Thread.currentThread().getName()))
                   .switchIfEmpty(Mono.error(new ResponseStatusException(
                           HttpStatus.NOT_FOUND, "No existe el empleado " + id)));
    }

    /**
     * TRAMPA CLASICA. Este metodo es igual que el de arriba pero SIN switchIfEmpty.
     *
     * Mucha gente da por hecho que un Mono vacio se convierte en un 404. NO ES ASI:
     * Spring responde 200 OK con el cuerpo vacio. Para el framework, "el flujo
     * termino sin emitir nada" es un final feliz, no un error.
     *
     * Compruebalo:
     *   curl -i http://localhost:8074/api/employees-suave/999   -> 200, cuerpo vacio
     *   curl -i http://localhost:8074/api/employees/999         -> 404, con mensaje
     *
     * Si quieres un 404, tienes que pedirlo tu con switchIfEmpty(). El vacio NO
     * es un error en el modelo reactivo: es simplemente un onComplete sin onNext.
     */
    @GetMapping("/employees-suave/{id}")
    public Mono<Employee> findByIdSuave(@PathVariable int id) {
        return repo.findById(id);
    }

    /**
     * El canal de error. Este endpoint SIEMPRE falla, para que veas como se maneja
     * un error sin un solo try/catch: el error viaja por el flujo, no por la pila.
     */
    @GetMapping("/employees/{id}/boom")
    public Mono<Employee> boom(@PathVariable int id) {
        return repo.findById(id)
                   .flatMap(e -> Mono.<Employee>error(new IllegalStateException("truena a proposito")))
                   .onErrorResume(ex -> {
                       log.warn("me lo comi: {}", ex.getMessage());
                       return Mono.just(new Employee(-1, "Plan", "B", "fallback@academymty.mx"));
                   });
    }

    /**
     * Un Mono<Void>: "no te devuelvo nada, solo te aviso cuando termine".
     * Es el equivalente reactivo de un metodo void.
     */
    @DeleteMapping("/employees/{id}")
    public Mono<Void> delete(@PathVariable int id) {
        return repo.findById(id).then();   // then() tira el valor y deja solo la senal de fin
    }
}
