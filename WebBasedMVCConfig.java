package org.patentprovider.config;

import org.patentprovider.dao.PatentDAO;
import org.patentprovider.dao.PatentDAOImpl;
import org.patentprovider.service.PatentService;
import org.patentprovider.service.PatentServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan(basePackages = { "org.patentprovider" })
public class WebBasedMVCConfig {
	/*@Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
 
        return viewResolver;
    }*/
	
	@Bean
	public View jsonTemplate() {
		MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setPrettyPrint(true);
        return view;
	}
	
	@Bean
	public ViewResolver viewResolver() {
		return new BeanNameViewResolver();
	}
	
	@Bean
	public DriverManagerDataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/");
		dataSource.setUsername("root");
		dataSource.setPassword("Mysuccess@26");
		return dataSource;
	}
	
	@Bean
	public PatentService getPatentService() {
		return new PatentServiceImpl();
	}
	
	@Bean
	public PatentDAO getPatentDAO() {
		return new PatentDAOImpl(getDataSource());
	}
}
