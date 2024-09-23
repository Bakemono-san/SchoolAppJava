package org.SchoolApp;


import org.SchoolApp.Datas.Entity.*;
import org.SchoolApp.Datas.Enums.EtatEnum;
import org.SchoolApp.Datas.Repository.EmargementRepository;
import org.SchoolApp.Datas.Repository.UserRepository;
import org.SchoolApp.Datas.Request.NoteRequest;
import org.SchoolApp.Datas.Request.NoteUpdate;
import org.SchoolApp.Datas.Request.PromoReferentiel;
import org.SchoolApp.Services.Impl.NoteService;
import org.SchoolApp.Services.Impl.PromoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.SchoolApp.Datas.Repository")
@EntityScan(basePackages = "org.SchoolApp.Datas.Entity")
@EnableTransactionManagement
public class SchoolAppJavaApplication implements WebMvcConfigurer {


	public static void main(String[] args) throws Exception {
		ApplicationContext context = SpringApplication.run(SchoolAppJavaApplication.class, args);



	}


}
