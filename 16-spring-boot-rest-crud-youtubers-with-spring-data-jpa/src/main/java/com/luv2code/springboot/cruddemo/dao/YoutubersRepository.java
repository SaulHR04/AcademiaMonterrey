package com.luv2code.springboot.cruddemo.dao;

import com.luv2code.springboot.cruddemo.entity.Youtubers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YoutubersRepository extends JpaRepository<Youtubers, Integer> {

    // that's it ... no need to write any code LOL!

}
