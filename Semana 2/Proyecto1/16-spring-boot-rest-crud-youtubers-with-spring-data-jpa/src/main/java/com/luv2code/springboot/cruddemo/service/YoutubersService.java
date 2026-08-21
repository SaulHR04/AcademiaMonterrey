package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Youtubers;

import java.util.List;

public interface YoutubersService {

    List<Youtubers> findAll();

    Youtubers findById(int theId);

    Youtubers save(Youtubers theYoutubers);

    void deleteById(int theId);

}
