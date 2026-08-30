# 3.1 WebFLUX

## WEbFlux - mono y flux
Es una implementacion de la programación reactiva para cuando se requiere situaciones donde la salida sea solamente uno. a comparación de flux que es una implementación que permite el manejo de elementos de salida de manera eficiente. 

**@SpringBootApplication** 

Arranque de la aplicación de spring.

```java
public record Employee(int id, String firstName, String lastName, String email) {
}
```
Creación del record de java que garantiza que el objeto no sea mutable y se mantenga inmutable.



### Implementación de mono en código
```java
public Mono<Employee> findById(int id) {
        return Mono.justOrEmpty(tabla.get(id))  
                   .delayElement(LATENCIA);      
    }

    public Employee findByIdBloqueante(int id) {
        try {
            Thread.sleep(LATENCIA.toMillis());   // el hilo se duerme, y no puede hacer nada mas
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return tabla.get(id);
    }
}
```
Busca el Id donde retorna solo objetos "Employee", con un delay de 5s por **DelayElement**.
**FindByIDBloqueante** Duerme el hilo con **thread.sleep** convertido a milisegundos. 

### RestControler.java
Aqui es donde se definen los end points reactivos de la api. 
```java
@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeRestController.class);

    private final EmployeeRepository repo;

    public EmployeeRestController(EmployeeRepository repo) {
        this.repo = repo;
    }
    @GetMapping("/employees/{id}")
    public Mono<Employee> findById(@PathVariable int id) {
        log.info("-> pediste el empleado {} (hilo: {})", id, Thread.currentThread().getName());

        return repo.findById(id)
                   .doOnNext(e -> log.info("<- llego {} (hilo: {})",
                           e.firstName(), Thread.currentThread().getName()))
                   .switchIfEmpty(Mono.error(new ResponseStatusException(
                           HttpStatus.NOT_FOUND, "No existe el empleado " + id)));
    }

     */
    @GetMapping("/employees-suave/{id}")
    public Mono<Employee> findByIdSuave(@PathVariable int id) {
        return repo.findById(id);
    }

    @GetMapping("/employees/{id}/boom")
    public Mono<Employee> boom(@PathVariable int id) {
        return repo.findById(id)
                   .flatMap(e -> Mono.<Employee>error(new IllegalStateException("truena a proposito")))
                   .onErrorResume(ex -> {
                       log.warn("me lo comi: {}", ex.getMessage());
                       return Mono.just(new Employee(-1, "Plan", "B", "fallback@academymty.mx"));
                   });
    }

    @DeleteMapping("/employees/{id}")
    public Mono<Void> delete(@PathVariable int id) {
        return repo.findById(id).then();   
    }
}
```

### WebFlux FLux
A diferencia del mono aqui trabaja con  0 1 elementos, flux permite trabajar con 0 a N elementos similar a lo que seria una lista.

**WebfluxFluxApplication.java**
```java
@SpringBootApplication
public class WebfluxFluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxFluxApplication.class, args);
    }
}
```
- Inicio de la aplicacion Spring.

**Lectura.java**
```java
public record Lectura(long numero, String sensor, double celsius, String hora) {
}
```
- Crea la estructura de los datos es un record, nuevamente inmutable.

**Creacion del flujo infinito**

```java

public Flux<Lectura> lecturas() {
        return Flux.interval(CADENCIA)
                   .map(this::medir);
    }
```

## En que momento usar WebFLUX mono/flux
Esto depende de la naturaleza de nuestra aplicacion/sistema.

**Mono:** Cuando las operaciónes devuelven 0 a 1 elementos. (Guardar, Actualizar, Eliminar)


**Flux:** Cuando las operaciónes devuelvan 1 a N elementos. Esto puede ser en listados.

## Que significa que sea Lazy:

Es decir que no se ejecuta el código hasta que existe explicitamente una peticion HTTP, si no recibe una petición no se activa y no pasa nada.