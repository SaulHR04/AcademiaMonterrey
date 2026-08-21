package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Youtubers;
import com.luv2code.springboot.cruddemo.repository.YoutubersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YoutubersServiceImpl implements YoutubersService {

    
    private YoutubersRepository YoutubersRepository;

    @Autowired
    public YoutubersServiceImpl(YoutubersRepository theEmployeeRepository) {
    	YoutubersRepository = theEmployeeRepository;
    }

    @Override
    public List<Youtubers> findAll() {
        return YoutubersRepository.findAll();
    }

    @Override
    public Youtubers findById(String theId) {

    
        return YoutubersRepository.findById(theId).orElse(null);
    }

    
    @Override
    public Youtubers save(Youtubers theEmployee) {
        return YoutubersRepository.save(theEmployee);
    }

    @Override
    public void deleteById(String theId) {
    	YoutubersRepository.deleteById(theId);
    }
}
