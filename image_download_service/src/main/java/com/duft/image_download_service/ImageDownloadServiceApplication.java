package com.duft.image_download_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableFeignClients(basePackages = "com.duft.image_download_service.FeignClient")
public class ImageDownloadServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageDownloadServiceApplication.class, args);
	}

}
