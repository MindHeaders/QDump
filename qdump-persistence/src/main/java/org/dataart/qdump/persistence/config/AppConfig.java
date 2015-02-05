package org.dataart.qdump.persistence.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", basePackages = "org.dataart.qdump")
@ComponentScan("org.dataart.qdump")
public class AppConfig {
	@Autowired
	private Environment env;
	@Autowired
	private DataSource dataSource;

	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource basicDataSource = new BasicDataSource(); 
		basicDataSource.setUrl(env.getProperty("db.url"));
		basicDataSource.setUsername(env.getProperty("db.user"));
		basicDataSource.setPassword(env.getProperty("db.password"));
		basicDataSource.setDriverClassName(env.getProperty("db.driver"));
		return basicDataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager(
			EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setPackagesToScan("org.dataart.qdump");
		bean.setDataSource(dataSource);
		Properties properties = new Properties();
		properties.put("hibernate.dialect",
				env.getProperty("hibernate.dialect"));
		properties.put("hibernate.hbm2ddl.auto",
				env.getProperty("hbm2ddl.auto"));
		properties.put("hibernate.show_sql", env.getProperty("show_sql"));
		bean.setJpaProperties(properties);
		JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		bean.setJpaVendorAdapter(adapter);
		return bean;
	}
}
