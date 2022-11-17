package net.kiwox.manager.dst.config;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.context.annotation.Bean;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

@org.springframework.context.annotation.Configuration
public class FreemarkerConfiguration {

	@Bean
	public Configuration freemarkerConfig() {
		Configuration cfg = new Configuration(new Version(2, 3, 20));
		cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "ftl");
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        cfg.setLocale(Locale.forLanguageTag("es"));
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
	}
	
}
