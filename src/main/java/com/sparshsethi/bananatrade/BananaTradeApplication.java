package com.sparshsethi.bananatrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BananaTradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BananaTradeApplication.class, args);
	}

}
