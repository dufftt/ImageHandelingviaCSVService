/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.image_download_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 *
 * @author souravsharma
 */
@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default() {
            @Override
            public Exception decode(String methodKey, Response response) {
                System.out.println("Feign error: " + response.status() + " - " + response.reason());
                return super.decode(methodKey, response);
            }
        };
    }
}