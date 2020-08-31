package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.demo.beans.DataTableRequestDTO;
import com.example.demo.beans.DataTablesResponseDTO;
import com.example.demo.service.ImageService;

import reactor.core.publisher.Mono;

import java.util.HashMap;

/**
 * WebFlux request handlers
 * 
 * @author legioner
 *
 */
@Component
public class WebFluxHandlerFunctions {

	@Autowired
	private ImageService  	imageService;
	
    public Mono<ServerResponse> login(ServerRequest request)
    {
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("login", new HashMap<>());
    }

    public Mono<ServerResponse> mainPage(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("index", new HashMap<String, Object>());
    }

    //ajax jquery datatable, returns Mono<DataTablesResponseDTO>
    public Mono<ServerResponse> imagesList(ServerRequest request) {
        return ServerResponse.ok()
  				.contentType(MediaType.APPLICATION_JSON)
  				.body(request.bodyToMono(DataTableRequestDTO.class)
  						.flatMap(req -> imageService.imageData(req)), DataTablesResponseDTO.class);
    }
    
}
