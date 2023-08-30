package project.demo;


import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import project.demo.interceptor.LogIntercreptor;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableCaching 
public class DemoApplication 
	implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		//tao seesion luu ngon ngu 
		SessionLocaleResolver resolver =
				new SessionLocaleResolver();
		resolver.setDefaultLocale(new Locale("vi"));
		return resolver;
	}
	
	@Bean 
	LocaleChangeInterceptor localeChangeInterceptor() {
		// loc cai req gui len xem chon nn nao
		LocaleChangeInterceptor lci =
			new LocaleChangeInterceptor();
		lci.setParamName("lang");
		
		return lci;
	}
	
	@Autowired
	LogIntercreptor logIntercreptor;
	
	// giong filter
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(logIntercreptor);
	}
	
}
