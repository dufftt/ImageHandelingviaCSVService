/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.csv_handeling.Utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.duft.csv_handeling.Entity.csvProduct;
import com.duft.csv_handeling.Entity.csvProductUrls;
import com.duft.csv_handeling.Entity.outputProductUrls;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 *
 * @author souravsharma
 */
public class CsvUtil {

    private static final CsvMapper mapper = new CsvMapper();

    public static <T> List <T> read(Class<T> clazz, InputStream stream) throws IOException {
        CsvSchema schema = mapper.schemaFor(clazz).withHeader().withColumnReordering(true);
        ObjectReader reader = mapper.readerFor(clazz).with(schema);
        return reader.<T>readValues(stream).readAll();
    }

    public List<csvProduct> parseCsv(MultipartFile file) throws IOException{
        List<csvProduct> products = new ArrayList<>();
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream(),StandardCharsets.UTF_8);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
        .setHeader("product_id","name","productUrl")
        .setSkipHeaderRecord(true)
        .setTrim(true)
        .build())){
            for(CSVRecord csvRecord: csvParser){
                int serialNumber = Integer.parseInt(csvRecord.get("product_id"));
                String productName = csvRecord.get("name");
                String productUrls = csvRecord.get("productUrl");

                List<String> urls = Arrays.asList(productUrls.split(","));
                csvProduct product = new csvProduct();
                List<csvProductUrls> csvproductUrls = new ArrayList<>();
                product.setProductSerialNumber(serialNumber);
                product.setProductName(productName);
                // for(String url: urls){
                //     csvProductUrls csvUrl = new csvProductUrls();
                //     csvUrl.setProductUrl(url);
                //     csvUrl.setProduct(product);
                //     csvproductUrls.add(csvUrl);
                    
                // }
                for (String url : urls) {
                    csvProductUrls csvUrl = new csvProductUrls();
                    csvUrl.setProductUrl(url);
                    csvUrl.setProduct(product); // Set the parent product
                    csvproductUrls.add(csvUrl);
                }
                product.setProductUrls(csvproductUrls);
                product.setProductUrls(csvproductUrls);
                // product.toString();
                products.add(product);
            }
        }
      
        return products;
    }



    public File generateCsvString(List<csvProduct> products, List<outputProductUrls> allOutputUrls) {
        StringBuilder csvBuilder = new StringBuilder();

        // 1. Create a lookup map for output URLs for efficient access
        // Key: inputUrlId (which is csvProductUrls.urlID)
        // Value: List of output URL strings
        String trackID="trackID";
        Map<Integer, List<String>> outputUrlsMap = new HashMap<>();
        if (allOutputUrls != null) {
            for (outputProductUrls outUrl : allOutputUrls) {
                // Ensure the URL string itself is not null before adding
                if (outUrl.getOutputUrls() != null && !outUrl.getOutputUrls().isEmpty()) {
                    outputUrlsMap.computeIfAbsent(outUrl.getInputUrlId(), k -> new ArrayList<>())
                                 .add(outUrl.getOutputUrls());
                }
            }
        }
        // 2. Add CSV Header
        csvBuilder.append("ProductSerialNumber,ProductName,ProductUrl,OutputImageUrls\n");

        // 3. Iterate through products and their URLs
        if (products != null && !products.isEmpty()) {
            for (csvProduct product : products) {
                List<String> currentProductUrlsList = new ArrayList<>();
                List<String> currentOutputUrlsList = new ArrayList<>();

                List<csvProductUrls> productUrls = product.getProductUrls(); // Assumes productUrls are loaded
                if (productUrls != null) {
                    for (csvProductUrls csvPUrl : productUrls) {
                        // Add current product's URL
                        if (csvPUrl.getProductUrl() != null && !csvPUrl.getProductUrl().isEmpty()) {
                            currentProductUrlsList.add(csvPUrl.getProductUrl());
                        }

                        // Get corresponding output URLs for this specific csvPUrl
                        List<String> specificOutputUrlStrings = outputUrlsMap.getOrDefault(csvPUrl.getUrlID(), Collections.emptyList());
                        currentOutputUrlsList.addAll(specificOutputUrlStrings);
                    }
                }

                // Join the collected URLs for the current product
                // The individual URLs are not escaped before joining, as they form content within a single CSV cell.
                // The entire joined string will be escaped by escapeCsvField if needed.
                String joinedProductUrls = String.join(",", currentProductUrlsList);
                String joinedOutputUrls = String.join(",", currentOutputUrlsList);

                // Append product details and the consolidated URL strings for the current product
                csvBuilder.append(escapeCsvField(String.valueOf(product.getProductSerialNumber()))).append(",");
                csvBuilder.append(escapeCsvField(product.getProductName())).append(",");
                csvBuilder.append(escapeCsvField(joinedProductUrls)).append(",");
                csvBuilder.append(escapeCsvField(joinedOutputUrls)).append("\n");
            }
             if (!products.isEmpty() && products.get(0).getTrackID() != null) {
                 trackID = products.get(0).getTrackID();
            }
        }

        try {
            File csvFile = File.createTempFile("outputCSV" + "_"+trackID, ".csv");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, StandardCharsets.UTF_8))) {
            writer.write(csvBuilder.toString());
            return csvFile;
        }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null; // Return null to indicate no file was created
        }
        
        
    }

    private String escapeCsvField(String data) {
        if (data == null) {
            return "";
        }
        if (data.contains(",") || data.contains("\"") || data.contains("\n") || data.contains("\r")) {
            return "\"" + data.replace("\"", "\"\"") + "\"";
        }
        return data;
    }

    
}
