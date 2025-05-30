package com.duft.csv_handeling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableFeignClients
public class CsvHandelingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvHandelingApplication.class, args);
	}

}
