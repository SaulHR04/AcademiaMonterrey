package com.luv2code.springboot.cruddemo.repository;

import com.luv2code.springboot.cruddemo.entity.Youtubers;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Aquí estaban EmployeeDAO y EmployeeDAOJpaImpl — unas 70 líneas de código.
 *
 * MongoRepository&lt;Employee, String&gt; ya trae implementados findAll(),
 * findById(), save() y deleteById(). Spring Data genera la implementación en
 * tiempo de arranque; no hay ninguna clase que escribir.
 *
 * Los dos parámetros son el tipo de la entidad y el tipo de su @Id.
 */
public interface YoutubersRepository extends MongoRepository<Youtubers, String> {

    // Sin cuerpo. Si más adelante necesitas una consulta propia, basta con
    // declarar el método siguiendo la convención de nombres, por ejemplo:
    //
    //     List<Employee> findByLastName(String lastName);
    //
    // Spring Data la implementa sola a partir del nombre.
}
