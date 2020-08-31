package com.example.demo.dao;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.demo.model.Image;

import reactor.core.publisher.Flux;

/**
 * Repository - returns Flux<Image>
 * 
 * @author legioner
 *
 */
public interface ImageRepository extends ReactiveCrudRepository<Image, Integer> {
	
	@Query("select * from images offset :from limit :size")
	Flux<Image> findAllFromTo(Integer from, Integer size);

}
