package com.ctc.demo.h2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan({"com.ctc.demo.h2.*"})
@SpringBootApplication
public class SampleOfH2JpaInSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleOfH2JpaInSpringBootApplication.class, args);
	}

}
