/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.csv_handeling.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

// import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.duft.csv_handeling.DTO.UrlsPayloadDTO;
import com.duft.csv_handeling.Entity.csvProduct;
import com.duft.csv_handeling.Entity.csvProductUrls;
import com.duft.csv_handeling.FeignClient.imageFeignClient;
import com.duft.csv_handeling.Services.InputCsvHandelingService;

import lombok.val;

/**
 *
 * @author souravsharma
 */
@RestController
public class InputCsvController {

    @Autowired
    public InputCsvHandelingService service;

    @Autowired
    public imageFeignClient client;

    // @PostMapping(value = "/uploadCsv" , consumes = "text/csv")
    // public void uploadCsv(@RequestBody InputStream csv) {
    //     System.out.println(csv);
    //     service.saveInputCsv(csv);
    //     }

    /*
     * Sample curl command: curl -X POST -F "file=@/Users/souravsharma/Downloads/test2.csv" http://localhost:9092/uploadCsv
     */
        @PostMapping(value = "/uploadCsv")
    public ResponseEntity<String> saveMultiPartCsv(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            return new ResponseEntity<>("Upload Correct file",HttpStatus.BAD_REQUEST);

        }
        String trackID = service.saveMultiPartCsv(file);
        if(!trackID.isEmpty()){
            String status = client.fetchAllByTrackingID(trackID);
            return new ResponseEntity<>("Uploaded Successfully with track id: "+trackID+"And process status: "+status,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Got into error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/fetchByTrackID/{trackID}")
    public ResponseEntity<List<csvProductUrls>> fetchAllByTrackingID(@PathVariable String trackID){
        try {

            return new ResponseEntity(service.fetchProducts(trackID),HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @PostMapping(value="/sendOutputData")
    public ResponseEntity<String> getPayLoad(@RequestBody UrlsPayloadDTO payload){
        return new ResponseEntity<>(service.savePayload(payload),HttpStatus.OK);
    }

    @GetMapping(value="/getcsv/{trackID}")
    public ResponseEntity<Resource> getCsv(@PathVariable String trackID){
        if(!service.checkProcessedInd(trackID).equals("Success")){
            return ResponseEntity.notFound().build();
        }else{
            
        

        try {
        File csvFile = service.fetchDataforCSV(trackID); // Call your service method

        if (csvFile == null || !csvFile.exists()) {
            // Handle case where file generation failed or file doesn't exist
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(csvFile));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + csvFile.getName() + "\"");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(csvFile.length())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);

    } catch (IOException e) {
        // Log error
        // e.printStackTrace(); 
        return ResponseEntity.internalServerError().body(null); // Or a more specific error response
    
    }
    }   
}

}