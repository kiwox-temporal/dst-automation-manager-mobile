package net.kiwox.manager.dst.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.retry.annotation.Retryable;

@Configuration
public class RetryableDataSourceConfiguration {

	@Bean
	public BeanPostProcessor dataSourceWrapper() {
		return new RetryableDataSourceBeanPostProcessor();
	}
	
	@Order(Ordered.HIGHEST_PRECEDENCE)
	private class RetryableDataSourceBeanPostProcessor implements BeanPostProcessor {

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			if ("dataSource".equals(beanName) && bean instanceof DataSource) {
				return new RetryableDataSource((DataSource) bean);
			}
			return bean;
		}
		
	}
	
	private class RetryableDataSource extends AbstractDataSource {
		
		private DataSource dataSource;

		public RetryableDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		@Override
		@Retryable(maxAttemptsExpression = "${datasource.retry.maxAttempts:3}")
		public Connection getConnection() throws SQLException {
			return dataSource.getConnection();
		}

		@Override
		@Retryable(maxAttemptsExpression = "${datasource.retry.maxAttempts:3}")
		public Connection getConnection(String username, String password) throws SQLException {
			return dataSource.getConnection(username, password);
		}
		
	}
	
}
