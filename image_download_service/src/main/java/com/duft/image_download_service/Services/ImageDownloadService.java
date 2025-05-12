/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.image_download_service.Services;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.duft.image_download_service.DTO.UrlsPayloadDTO;
import com.duft.image_download_service.Entities.csvProductUrls;
import com.duft.image_download_service.Entities.outputProductUrls;
import com.duft.image_download_service.FeignClient.csvFeignClient;
import com.duft.image_download_service.Repository.CsvProductUrlsRepositry;
import com.duft.image_download_service.Repository.OutputProductUrlRepository;
import com.duft.image_download_service.Utility.Utility;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

/**
 *
 * @author souravsharma
 */
@Service
public class ImageDownloadService {

    @Autowired
    private csvFeignClient client;
    // @Autowired
    // private csvProductRepo repo;
    @Autowired
    private Utility utility;
    @Autowired
    private OutputProductUrlRepository outputRepo;
    @Autowired
    private CsvProductUrlsRepositry productUrlrepo;

    private static final Log logger = LogFactory.getLog(ImageDownloadService.class);
    

    @PostConstruct
    public void init() {
        System.out.println("Feign client injected: " + (client != null));
    }

    public void fetchRecords(String trackID){
        System.out.println("inside service pre feign client");
        //ResponseEntity<List<csvProduct>> response = client.fetchProductByTrackID(trackID);
        //List<csvProduct> products = response.getBody();
        try {
            List<csvProductUrls> products = client.fetchProductByTrackID(trackID);
        System.out.println("inside services post feign client");
        this.savecsvProducts(products);
        System.out.println("post save in db");
        this.fetchProductUrls();
        
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            
        }
        
    }

    @Transactional
    public void fetchProductUrls(){
        List<csvProductUrls> urls = productUrlrepo.findByProcessedIndFalse();
        for(csvProductUrls url: urls){
             //System.out.println("URL: "+url.toString());
            try {
               String outputUrl = utility.downloadAndCompressImage(url.getProductUrl());
               System.out.println("Output UR: "+outputUrl);
                 outputProductUrls outputProductUrls = new outputProductUrls();
                outputProductUrls.setInputUrlId(url.getUrlID());
                outputProductUrls.setOutputUrls(outputUrl);
                outputRepo.save(outputProductUrls);
                url.setProcessedInd(true);
                //System.out.println("After updating processed ind: "+url.toString());
                productUrlrepo.save(url); // Save the updated entity back to the database
                //outputUrlsList.add(outputProductUrls);
               //TODO correct below to update url and then add it back in product to commit it
               //urls.get(0).setProcessedInd(true);
            } catch (IOException ex) {
            }
               
            }  
            
    }

    @Async
    public void retryProcessing(){
        this.fetchProductUrls();
        List<csvProductUrls> inputUrls = productUrlrepo.findByProcessedIndTrue();
        List<outputProductUrls> outputurls = outputRepo.findAll();
        UrlsPayloadDTO payload = new UrlsPayloadDTO(inputUrls, outputurls);
        //TODO uncomment below after creating async method to handle below in csvservice
        client.sendPayload(payload);
    }

    @Async
    public void startdownloadService(String trackID){
        this.fetchRecords(trackID);
        List<csvProductUrls> inputUrls = productUrlrepo.findByProcessedIndTrue();
        List<outputProductUrls> outputurls = outputRepo.findAll();
        UrlsPayloadDTO payload = new UrlsPayloadDTO(inputUrls, outputurls);
        //TODO uncomment below after creating async method to handle below in csvservice
        client.sendPayload(payload);
    }

    private void sendDatatoCsv( List<csvProductUrls> inputUrls, List<outputProductUrls> outputurls){
        //TODO make feign client call to csvservice and send both the list
    }


    // public void downloadAndCompressImage(String trackID){
    //     try {
    //         List<csvProduct> products = client.fetchProductByTrackID(trackID);
    //         List<outputProductUrls> outputUrlsList = new ArrayList<>();
    //     for(csvProduct product: products){
    //         List<csvProductUrls> urls = product.getProductUrls();
    //         for(csvProductUrls url: urls){
    //          String  outputUrl =  utility.downloadAndCompressImage(url.getProductUrl());
    //             outputProductUrls outputProductUrls = new outputProductUrls();
    //             outputProductUrls.setInputUrlId(url.getUrlID());
    //             outputProductUrls.setOutputUrls(outputUrl);
    //             outputRepo.save(outputProductUrls);
    //             outputUrlsList.add(outputProductUrls);
    //            //TODO correct below to update url and then add it back in product to commit it
    //            //urls.get(0).setProcessedInd(true);
    //         }  
    //     }
    //     } catch (IOException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    // }


    @Transactional
    public void savecsvProducts(List<csvProductUrls> productUrls) {
        try {
            
                for(csvProductUrls url: productUrls){
                    csvProductUrls ur = new csvProductUrls();
                    ur.setUrlID(url.getUrlID());
                    ur.setProcessedInd(url.isProcessedInd());
                    ur.setProductUrl(url.getProductUrl());
                    //ur.setProduct(url.getProduct());
                    productUrlrepo.save(ur);
                }
                //System.out.println(Arrays.toString(csvproductUrls.toArray()));
                //System.out.println("Inside save product: productName: "+product.getProductName()+" serial number: "+product.getProductSerialNumber()+" track id: "+product.getTrackID()+" product id: "+product.getProductID());
                
            }
        catch(Exception e){
            // TODO: handle exception
            e.printStackTrace();
            logger.debug("Error in save");
        }
            }

}
