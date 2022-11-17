package net.kiwox.manager.dst;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.kiwox.manager.dst.service.impl.CustomMailServiceImpl;
import net.kiwox.manager.dst.service.interfaces.ICustomMailService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableRetry
public class Application implements ServletContextListener {

	private static final Log LOG = LogFactory.getLog(Application.class);

	@Bean
	public ServletContextListener listener() {
		return this;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOG.info("ServletContext initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOG.info("ServletContext destroyed");
	}
}
