package org.dataart.qdump.persistence.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

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

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory")
@ComponentScan("org.dataart.qdump")
public class AppConfig {
	@Autowired
	private Environment env;
	@Autowired
	private DataSource dataSource;

	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.user"));
		dataSource.setPassword(env.getProperty("db.password"));
		dataSource.setDriverClassName(env.getProperty("db.driver"));
		return dataSource;
	}

	/*@Bean
	public PlatformTransactionManager transactionManagerSessionFactory(
			SessionFactory sessionFactory) {
		return new HibernateTransactionManager(sessionFactory);
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setPackagesToScan("org.dataart.qdump");
		Properties properties = new Properties();
		properties.put("hibernate.dialect",
				env.getProperty("hibernate.dialect"));
		properties.put("hibernate.hbm2ddl.auto",
				env.getProperty("hbm2ddl.auto"));
		properties.put("hibernate.show_sql", env.getProperty("show_sql"));
		bean.setHibernateProperties(properties);
		bean.setEntityInterceptor(new GlobalInterceptor());
		return bean;
	}*/

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
