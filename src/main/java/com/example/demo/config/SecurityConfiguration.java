package com.example.demo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;

/**
 * Security configuration
 * Rsocket and webflux in one class
 * 
 * @author legioner
 *
 */
@Configuration
@EnableRSocketSecurity
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

	//WebFlux
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .formLogin()
                .loginPage("/login")
                .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/"))
                .and()
                .logout()
                .and()
                .authorizeExchange()
                .pathMatchers("/login",
                        "/logout",
                        "/img/**",
                        "/built/**",
                        "/webjars/**")
                .permitAll()
                .pathMatchers("/**")
                .authenticated()
                .and()
                .csrf().disable();
        return http.build();
    }

    //Common - user service
    @Bean
    public MapReactiveUserDetailsService userDetailsServiceProd() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("test")
                .roles("user")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    //RSocket message Handler. Add resolver for AuthenticationPrincipal - it could be used in rsocket controller methods
    @Bean 
    public RSocketMessageHandler messageHandler(RSocketStrategies strategies) {

        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        handler.setRSocketStrategies(strategies);
        return handler;
    }

    //Rsocket security for Java client (setup - authenticated; other - not)
    @Bean
    @ConditionalOnProperty(name="apllication.testmode", havingValue="true")
    public PayloadSocketAcceptorInterceptor authorizationTest(RSocketSecurity security) {
        security.authorizePayload(authorize ->
                authorize
                        .setup().authenticated()
                        .anyExchange().permitAll() 
        ).simpleAuthentication(Customizer.withDefaults());
        return security.build();
    }
    
    //Rsocket security for javascript client. 
    //Issue with send username/password from client - all - unauthenticated
    @Bean
    @ConditionalOnProperty(name="apllication.testmode", havingValue="false")
    public PayloadSocketAcceptorInterceptor authorization(RSocketSecurity security) {
    	security.authorizePayload(authorize ->
    		authorize.setup().permitAll()
    		.anyExchange().permitAll() 
		).simpleAuthentication(Customizer.withDefaults());
    	return security.build();
    }



}
