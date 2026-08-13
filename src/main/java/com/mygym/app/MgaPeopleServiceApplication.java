package com.mygym.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;

@SpringBootApplication(exclude = {
	    MongoAutoConfiguration.class
	})
public class MgaPeopleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MgaPeopleServiceApplication.class, args);	
	}

}
