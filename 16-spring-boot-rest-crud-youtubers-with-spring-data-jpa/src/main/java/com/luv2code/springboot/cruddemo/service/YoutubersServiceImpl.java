package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.dao.YoutubersRepository;
import com.luv2code.springboot.cruddemo.entity.Youtubers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YoutubersServiceImpl implements YoutubersService {

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
    public Youtubers findById(int theId) {
        Optional<Youtubers> result = YoutubersRepository.findById(theId);

        Youtubers theYoutubers = null;

        if (result.isPresent()) {
            theYoutubers = result.get();
        }
        else {
            throw new RuntimeException("No se encontro id Youtubers - " + theId);
        }

        return theYoutubers;
    }

    @Override
    public Youtubers save(Youtubers theYoutubers) {
        return YoutubersRepository.save(theYoutubers);
    }

    @Override
    public void deleteById(int theId) {
    	YoutubersRepository.deleteById(theId);
    }
}






