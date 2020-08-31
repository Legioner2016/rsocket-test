package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.DatabaseClient.GenericExecuteSpec;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.beans.DataTableRequestDTO;
import com.example.demo.beans.DataTablesResponseDTO;
import com.example.demo.dao.ImageRepository;
import com.example.demo.model.Image;

import reactor.core.publisher.Mono;

/**
 * ImageService return Mono<DataTablesResponseDTO<Image>> (instead of Flux<Image>) - 
 * to work as datasource for jquery datatable
 * 
 * @author legioner
 *
 */
@Service
public class ImageService {
	
	@Autowired
	private ImageRepository		repository;
	
	private final DatabaseClient db;
	
	public ImageService(DatabaseClient db) {
		this.db = db;
	}
	
	@Transactional
	public Mono<DataTablesResponseDTO<Image>> imageData(DataTableRequestDTO request) {
		return Mono.zip(
				usersList(request),
				repository.count()
				)
				.map(lp -> {
					DataTablesResponseDTO<Image> response = new DataTablesResponseDTO<>();
					response.setDraw(request.getDraw());
					response.setData(lp.getT1());
					response.setRecordsTotal(lp.getT2());
					response.setRecordsFiltered(lp.getT2());
					return response;
				});
	}
	
	private Mono<List<Image>> usersList(DataTableRequestDTO request) {
		String query = String.format("select * from images offset %d limit %d", request.getStart(), request.getLength());
		GenericExecuteSpec exec = db.execute(query);
		return exec.map(Image::new).all().collect(Collectors.toList());	
	}
	
	

}
