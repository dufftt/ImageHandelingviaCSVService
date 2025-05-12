/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.duft.image_download_service.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.duft.image_download_service.Config.FeignConfig;
import com.duft.image_download_service.DTO.UrlsPayloadDTO;
import com.duft.image_download_service.Entities.csvProductUrls;

/**
 *
 * @author souravsharma
 */
@FeignClient(name="csvProduct", url = "${feign.url-csv-handle-service}", configuration = FeignConfig.class)
public interface csvFeignClient {

    @GetMapping(value="/fetchByTrackID/{trackID}")
    public List<csvProductUrls> fetchProductByTrackID(@PathVariable(name="trackID") String trackID);
    @PostMapping(value="/sendOutputData")
    public String sendPayload(@RequestBody UrlsPayloadDTO payload);
}
