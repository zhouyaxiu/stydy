package com.shensu.jsjm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("com.shensu.jsjm.mapper")
@ServletComponentScan(basePackages = "com.shensu.jsjm")
public class VisualSystemApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(VisualSystemApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(VisualSystemApplication.class);
	}

}
