/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.duft.csv_handeling.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author souravsharma
 */
@FeignClient(name="csvHandle", url = "${feign.url-image-download-service}")
public interface imageFeignClient {

    @GetMapping(value="/fetchByTrackID/{trackID}")
    public String fetchAllByTrackingID(@PathVariable String trackID);

    @GetMapping(value="/retry")
    public String retryProcessing();
}
