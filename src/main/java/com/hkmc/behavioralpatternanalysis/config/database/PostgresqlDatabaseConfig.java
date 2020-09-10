package com.hkmc.behavioralpatternanalysis.config.database;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.transaction.ReactiveTransactionManager;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.ValidationDepth;

//@Configuration
//@EnableR2dbcRepositories
public class PostgresqlDatabaseConfig extends AbstractR2dbcConfiguration {

	private static final String POSTGRESQL = "postgresql";

	private static final String POOL = "pool";
	
	@Value("${spring.postgres.uri}")
	private String host;
	@Value("${spring.postgres.port}")
	private int port;
	@Value("${spring.postgres.database}")
	private String database;
	@Value("${spring.postgres.username}")
	private String usernamee;
	@Value("${spring.postgres.password}")
	private String password;
	@Value("${spring.postgres.pool.name}")
	private String poolname;
	@Value("${spring.postgres.pool.maxidletime}")
	private int maxIdleTime;
	@Value("${spring.postgres.pool.maxidletime}")
	private int maxLifeTime;
	@Value("${spring.postgres.pool.initalsize}")
	private int initalSize;
	@Value("${spring.postgres.pool.max-size}")
	private int maxSize;
	@Value("${spring.postgres.pool.maxcreateconnectiontime}")
	private int maxCreateConnectionTime;
	
	private static DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(new PostgresDialect());
	
	@Override
	@Bean
	public ConnectionFactory connectionFactory() {
		ConnectionFactory connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
				.option(ConnectionFactoryOptions.DRIVER, POOL)
				.option(ConnectionFactoryOptions.PROTOCOL, POSTGRESQL)
				.option(ConnectionFactoryOptions.HOST, host)
				.option(ConnectionFactoryOptions.PORT, port)
				.option(ConnectionFactoryOptions.DATABASE, database)
				.option(ConnectionFactoryOptions.USER, usernamee)
				.option(ConnectionFactoryOptions.PASSWORD, password)
				.build());
		ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
				.initialSize(initalSize)
				.maxSize(maxSize)
				.maxIdleTime(Duration.ofMinutes(maxIdleTime))
				.maxLifeTime(Duration.ofMinutes(maxLifeTime))
				.maxCreateConnectionTime(Duration.ofMillis(maxCreateConnectionTime))
				.validationDepth(ValidationDepth.LOCAL)
				.validationQuery("select 1+1")
				.name(poolname)
				.build();
		
		return new ConnectionPool(configuration);
	}
	
	@Bean
	public R2dbcEntityOperations postgresqlEntityOperations(ConnectionFactory connectionFactory) {
		DatabaseClient databaseClient = DatabaseClient.builder()
				.connectionFactory(connectionFactory)
				.dataAccessStrategy(strategy)
				.build();
		
		strategy.getMappingContext();
		return new R2dbcEntityTemplate(databaseClient, strategy);
	}
	
	@Bean
	public R2dbcRepositoryFactory postgresqlRepositoryFactory(ConnectionFactory connectionFactory) {
		DatabaseClient databaseClient = DatabaseClient.builder()
				.connectionFactory(connectionFactory)
				.dataAccessStrategy(strategy)
				.build();
		return new R2dbcRepositoryFactory(databaseClient, strategy);
	}
	
	@Bean
	public R2dbcConverter r2dbcConverter() {
		return strategy.getConverter();
	}
	
	@Bean
	ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
		return new R2dbcTransactionManager(connectionFactory);
	}

}
