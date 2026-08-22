package com.luv2code.springboot.cruddemo.repository;

import com.luv2code.springboot.cruddemo.entity.Youtubers;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface YoutubersRepository extends MongoRepository<Youtubers, String> {

}
