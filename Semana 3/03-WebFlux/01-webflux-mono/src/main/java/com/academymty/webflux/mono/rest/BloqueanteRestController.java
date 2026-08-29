package com.academymty.webflux.mono.rest;

import com.academymty.webflux.mono.model.Employee;
import com.academymty.webflux.mono.repo.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

/**
 * ATENCION: esto es LO QUE NO HAY QUE HACER.
 *
 * Es el controlador del proyecto 15 copiado tal cual dentro de una app WebFlux.
 * Compila, funciona, y devuelve el mismo JSON... hasta que llegan varias peticiones
 * a la vez. Entonces se hunde, porque cada peticion duerme uno de los poquitos
 * hilos del event loop (4-8 en total), y mientras duerme NADIE mas es atendido.
 *
 * Esta aqui solo para que scripts/comparar.sh pueda medir la diferencia.
 * Borralo de tu cabeza en cuanto veas los numeros.
 */
@RestController
@RequestMapping("/api/mvc")
public class BloqueanteRestController {

    private final EmployeeRepository repo;

    public BloqueanteRestController(EmployeeRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/employees/{id}")
    public Employee findByIdBloqueante(@PathVariable int id) {
        return repo.findByIdBloqueante(id);   // Thread.sleep() sobre el event loop
    }
}
