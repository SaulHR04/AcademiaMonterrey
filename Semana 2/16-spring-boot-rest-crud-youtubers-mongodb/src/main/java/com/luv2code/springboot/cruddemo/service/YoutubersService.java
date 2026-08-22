package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Youtubers;

import java.util.List;

public interface YoutubersService {

    List<Youtubers> findAll();

    Youtubers findById(String theId);

    Youtubers save(Youtubers theEmployee);

    void deleteById(String theId);

}
