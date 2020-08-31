package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * WebFlux routes
 * 
 * @author legioner
 *
 */
@Configuration
public class WebFluxRoutes {

    @Autowired
    private WebFluxHandlerFunctions handlers;

    @Bean
    public RouterFunction<ServerResponse> routes() {
        //return RouterFunctions.resources("/img/**", new ClassPathResource("/static/img/"))
    	return RouterFunctions.route(GET("/login"), handlers::login)
                .and(
                        RouterFunctions.route(GET("/"), handlers::mainPage)
                )
                .and(
						RouterFunctions.route(POST("/imagesList"), handlers::imagesList) 
				)
                ;
    }

}
