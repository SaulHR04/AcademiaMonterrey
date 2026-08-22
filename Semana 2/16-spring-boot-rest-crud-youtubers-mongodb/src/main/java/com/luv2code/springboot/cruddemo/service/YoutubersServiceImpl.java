package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Youtubers;
import com.luv2code.springboot.cruddemo.repository.YoutubersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YoutubersServiceImpl implements YoutubersService {

    // Antes se inyectaba EmployeeDAO. Ahora es el repositorio de Spring Data,
    // que no tiene implementación escrita a mano.
    private YoutubersRepository YoutubersRepository;

    @Autowired
    public YoutubersServiceImpl(YoutubersRepository theYoutubersRepository) {
    	YoutubersRepository = theYoutubersRepository;
    }

    @Override
    public List<Youtubers> findAll() {
        return YoutubersRepository.findAll();
    }

    @Override
    public Youtubers findById(String theId) {

        // findById() devuelve Optional<Employee>. Lo convertimos a null para
        // conservar el mismo contrato que tenía la versión con JPA: el
        // controlador sigue comprobando "if (tempEmployee == null)".
        return YoutubersRepository.findById(theId).orElse(null);
    }

    // Ojo: aquí ya no hay @Transactional.
    //
    // MongoDB en modo standalone (un contenedor suelto, sin replica set) no
    // soporta transacciones multi-documento. Y no hacen falta: cada operación
    // toca un solo documento, y MongoDB garantiza atomicidad por documento.
    @Override
    public Youtubers save(Youtubers theYoutubers) {
        return YoutubersRepository.save(theYoutubers);
    }

    @Override
    public void deleteById(String theId) {
    	YoutubersRepository.deleteById(theId);
    }
}
