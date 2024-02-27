package com.spring.guide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class SpringGuideApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringGuideApplication.class, args);
	}

}
