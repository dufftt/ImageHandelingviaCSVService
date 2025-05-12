/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.csv_handeling.Services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.duft.csv_handeling.DTO.UrlsPayloadDTO;
import com.duft.csv_handeling.Entity.csvProduct;
import com.duft.csv_handeling.Entity.csvProductUrls;
import com.duft.csv_handeling.Entity.outputProductUrls;
import com.duft.csv_handeling.FeignClient.imageFeignClient;
import com.duft.csv_handeling.Repository.InputUrlRepository;
import com.duft.csv_handeling.Repository.OutputUrlRepository;
import com.duft.csv_handeling.Repository.csvProductRepository;
import com.duft.csv_handeling.Utility.CsvUtil;

import ch.qos.logback.core.net.server.Client;


/**
 *
 * @author souravsharma
 */
@Service
public class InputCsvHandelingService {

    @Autowired
    private csvProductRepository repo;
    @Autowired
    private InputUrlRepository inputRepo;
    @Autowired
    private OutputUrlRepository outputRepo;
    @Autowired
    private imageFeignClient client;
   

    @Transactional
    public String savePayload(UrlsPayloadDTO urlsDTO){
       List<csvProductUrls> inputUrls = urlsDTO.getInputUrls(); // Assuming inputUrls is a list of strings
       List<outputProductUrls> outputUrls = urlsDTO.getOutputUrls();
        try {
            for(csvProductUrls inputUrl: inputUrls){
                inputRepo.updateProcessedIndByUrlID(inputUrl.getUrlID(),inputUrl.isProcessedInd());
            }
            System.out.println("processed ind is updated");
            for(outputProductUrls outputUrl: outputUrls){
                System.out.println("output: "+outputUrl.getOutputUrls());
                outputProductUrls out = new outputProductUrls();
                out.setInputUrlId(outputUrl.getInputUrlId());
                out.setOutputUrlId(outputUrl.getOutputUrlId());
                out.setOutputUrls(outputUrl.getOutputUrls());
                outputRepo.save(out);
                System.out.println("output is saved"+out.getOutputUrls());
            }
            return "Success";
        } catch (Exception e) {
            // TODO: handle exception
            return "Failed to update Data";
        }
    }

    public File fetchDataforCSV(String trackID){
        CsvUtil util = new CsvUtil();
        List<csvProduct> products = repo.findByTrackID(trackID);
        // List<outputProductUrls outputUrls = outputRepo.findByTrackID();
        List<Integer> inputURLID = repo.findProductUrlIDListByTrackID(trackID);
        List<outputProductUrls> outputUrlList = new ArrayList<>();
        for(Integer URLID: inputURLID){
            List<outputProductUrls> output = outputRepo.findByInputUrlId(URLID);
            outputUrlList.addAll(output);
        }
        //select * from 
       return util.generateCsvString(products, outputUrlList);
    }

    public String checkProcessedInd(String trackID){
      List<Boolean> processedList =  repo.findProcessedListByTrackID(trackID);
      if(processedList.contains(false)){
        System.out.println("Inside processed ind: false");
        client.retryProcessing();
        return "Kindly retry after sometime";
      }else{
        System.out.println("Inside processed ind: true");
        return "Success";
      }
    }

    @Transactional
    public String saveMultiPartCsv(MultipartFile file){
        try {
            CsvUtil util = new CsvUtil();
            String trackID = this.generateTrackID();
           List<csvProduct> products =  util.parseCsv(file);
            for(csvProduct product: products){
                product.setTrackID(trackID);
                repo.save(product);
            }
        //   repo.saveAll(products);
          return products.get(0).getTrackID();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    public List<csvProductUrls> fetchProducts(String trackID){
        return repo.findProductUrlsListByTrackID(trackID);
    }

    private String generateTrackID(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
    // Take a portion (e.g., first 6 hex characters)
    String shortUuid = uuid.substring(0, 6);
    // Attempt to convert to a decimal number (might not always be 6 digits)
    try {
        long decimalValue = Long.parseLong(shortUuid, 16);
        String randomValue = String.format("%06d", decimalValue % 1000000); // Ensure 6 digits with leading zeros
        return "CSV"+randomValue;
    } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
    }
}

}
