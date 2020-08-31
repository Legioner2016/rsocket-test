package com.example.demo.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.WebJarsResourceResolver;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Additional WebFlux View Resolver configuration
 * 
 * @author legioner
 *
 */
@Configuration
@EnableWebFlux
public class WebFluxConfig implements ApplicationContextAware, WebFluxConfigurer {

	private ApplicationContext ctx;
	
	@Override
	public void setApplicationContext(ApplicationContext context) {
		this.ctx = context;
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.viewResolver(thymeleafChunkedAndDataDrivenViewResolver());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler("/webjars/**")
		.addResourceLocations("classpath:/META-INF/resources/webjars/")
		.setCacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
		.resourceChain(true)
		.addResolver(new WebJarsResourceResolver());
		
		registry
		.addResourceHandler("/built/**")
		.addResourceLocations("classpath:/static/built/")
		.setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
		.resourceChain(true);
	}
	
	@Bean
	public SpringResourceTemplateResolver thymeleafTemplateResolver() {

		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(this.ctx);
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCacheable(false);
		resolver.setCheckExistence(false);
		return resolver;

	}

	@Bean
	public ISpringWebFluxTemplateEngine thymeleafTemplateEngine() {
		SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
		templateEngine.setTemplateResolver(thymeleafTemplateResolver());
		templateEngine.addDialect(new Java8TimeDialect());
		return templateEngine;
	}

	@Bean
	public ThymeleafReactiveViewResolver thymeleafChunkedAndDataDrivenViewResolver() {
		final ThymeleafReactiveViewResolver viewResolver = new ThymeleafReactiveViewResolver();
		viewResolver.setTemplateEngine(thymeleafTemplateEngine());
		viewResolver.setResponseMaxChunkSizeBytes(16384); 
		return viewResolver;
	}
	

}