/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.image_download_service.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.duft.image_download_service.FeignClient.csvFeignClient;
import com.duft.image_download_service.Services.ImageDownloadService;

/**
 *
 * @author souravsharma
 */
@RestController
public class ImageDownloadController {

    @Autowired
    private ImageDownloadService service;

     @Autowired
    private csvFeignClient client;


    @GetMapping(value = "/fetchByTrackID/{trackID}")
    public ResponseEntity<String> fetchAllByTrackingID(@PathVariable String trackID){
        try {
            System.out.println("inside controller");
            //client.fetchProductByTrackID(trackID);
            //List<csvProduct> products = service.fetchRecords(trackID);
            service.startdownloadService(trackID);
            return new ResponseEntity("Success",HttpStatus.ACCEPTED);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        
    }
    @GetMapping(value="/retry")
    public ResponseEntity<String> retryProcessing(){
        service.retryProcessing();
        return new ResponseEntity<>("Success Retry",HttpStatus.OK);
    }
}
