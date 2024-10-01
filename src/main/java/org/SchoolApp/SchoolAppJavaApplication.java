package org.SchoolApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = {"org.SchoolApp", "org.odc.core"})
@EnableJpaRepositories(basePackages = "org.SchoolApp.Datas.Repository")
@EntityScan(basePackages = "org.SchoolApp.Datas.Entity")
@EnableTransactionManagement
public class SchoolAppJavaApplication implements WebMvcConfigurer {
	public static void main(String[] args) throws Exception {
		ApplicationContext context = SpringApplication.run(SchoolAppJavaApplication.class, args);
	}
}
