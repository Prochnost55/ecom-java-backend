package com.prochnost.ecom.backend;

import com.prochnost.ecom.backend.service.initService.InitService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private InitService initService;

	public Application(InitService initService) {
		this.initService = initService;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		initService.initialise();
	}
}
