package com.aymen.doc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DocDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocDemoApplication.class, args);
	}

}
