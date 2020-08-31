package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;

import static io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

import java.time.Duration;

/**
 * Database configuration (r2dbc)
 * 
 * @author legioner
 *
 */
@Configuration
@EnableR2dbcRepositories(basePackages = "com.example.demo.dao") //Use one repository in project
public class DatabaseConfiguration {

	@Value("${spring.datasource.host}")
	private String 	host;
	@Value("${spring.datasource.port}")
	private String port;
	@Value("${spring.datasource.database}")
	private String 	database;
	@Value("${spring.datasource.username}")
	private String 	username;
	@Value("${spring.datasource.password}")
	private String 	password;

	@Value("${spring.datasource.max_pool_size:3}")
	private String POOL_MAX_SIZE;
	private static final int POOL_START_SIZE = 1;
	private static final String POOL_DRIVER = "pool";
	private static final String POOL_PROTOCOL = "postgresql";

	@Bean
	public ConnectionFactory connectionFactory() {
		ConnectionFactory connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
				.option(DRIVER, POOL_DRIVER)
				.option(PROTOCOL, POOL_PROTOCOL)
				.option(HOST, host)
				.option(USER, username)
				.option(PASSWORD, password)
				.option(DATABASE, database)
				.option(MAX_SIZE, Integer.parseInt(POOL_MAX_SIZE))
				.option(CONNECT_TIMEOUT, Duration.ofSeconds(20L))
				.build());
		ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
				.maxIdleTime(Duration.ofMinutes(1))
				.initialSize(POOL_START_SIZE)
				.maxSize(Integer.parseInt(POOL_MAX_SIZE))
				.maxCreateConnectionTime(Duration.ofSeconds(1))
				.build();
		return new ConnectionPool(configuration);
	}
	
    @Bean
    public DatabaseClient r2dbcDatabaseClient() {
        return DatabaseClient.builder().connectionFactory(connectionFactory()).build();
    }

    @Bean
    public DefaultReactiveDataAccessStrategy reactiveDataAccessStrategy() {
    	return new DefaultReactiveDataAccessStrategy(new  PostgresDialect());
    }
    

}
