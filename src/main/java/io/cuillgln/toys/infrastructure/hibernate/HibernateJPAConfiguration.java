
package io.cuillgln.toys.infrastructure.hibernate;

import io.cuillgln.toys.infrastructure.jdbc.HikariCPProperties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@EnableConfigurationProperties(HikariCPProperties.class)
@Configuration
public class HibernateJPAConfiguration {

	private HikariCPProperties properties;

	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(properties.getJdbcUrl());
		config.setUsername(properties.getUsername());
		config.setPassword(properties.getPassword());
		config.setAutoCommit(properties.isAutoCommit());
		config.setIdleTimeout(properties.getIdleTimeout());
		config.setMaxLifetime(properties.getMaxLifeTime());
		config.setMinimumIdle(properties.getMinimumIdle());
		config.setMaximumPoolSize(properties.getMaximumPoolSize());
		config.setValidationTimeout(properties.getValidationTimeout());
		return new HikariDataSource(config);
	}

	@Bean
	public SessionFactory sessionFactory() {
		BootstrapServiceRegistry bootstrapRegistry =
						new BootstrapServiceRegistryBuilder().build();
		StandardServiceRegistryBuilder standardRegistryBuilder =
						new StandardServiceRegistryBuilder(bootstrapRegistry)
										.applySetting(AvailableSettings.DATASOURCE, dataSource());
		StandardServiceRegistry standardRegistry = standardRegistryBuilder.build();
		MetadataSources sources = new MetadataSources(standardRegistry);
		Metadata metadata = sources.buildMetadata();
		SessionFactory sessionFactory = metadata.buildSessionFactory();
		return sessionFactory;
	}

}
