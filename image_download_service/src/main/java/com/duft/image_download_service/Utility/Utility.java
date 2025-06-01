/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.image_download_service.Utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.stereotype.Service;
/**
 *
 * @author souravsharma
 */
@Service
public class Utility {

   private static final String UPLOAD_DIR = "images"; // Default upload directory
    // @org.springframework.beans.factory.annotation.Value("${image.upload.dir}")
    // private String UPLOAD_DIR;

    // @PostConstruct
    // private void initUploadDir() {
    //     Path uploadPath = Paths.get(UPLOAD_DIR);
    //     if (!Files.exists(uploadPath)) {
    //         try {
    //             Files.createDirectories(uploadPath);
    //         } catch (IOException e) {
    //             throw new RuntimeException("Could not create upload directory!", e);
    //         }
    //     }
    // }
    private static final float COMPRESSED_QUALITY = 0.7f;
    

    public Utility(){
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory!", e);
            }
        }
    }

    public String downloadAndCompressImage(String imageUrl) throws IOException{
       BufferedImage image = downloadImage(imageUrl);
       if (image == null) {
            throw new IOException("Could not download image from URL: " + imageUrl);
        }
        File compressedImage = CompressImage(image, COMPRESSED_QUALITY);
        return compressedImage.getAbsolutePath();
    }

    private BufferedImage downloadImage(String imageUrl) throws IOException{

        URL url = URI.create(imageUrl).toURL();
        try(InputStream inputStream = url.openStream()){
            return ImageIO.read(inputStream);

        }catch(IOException e){
         System.err.println("Failed to download image using java.net.URL: "+imageUrl+"Error: " + e.getMessage());

           throw e;
        }
        // RestTemplate restTemplate = new RestTemplate();
        // ResponseEntity<byte[]> response = restTemplate.exchange(
        //     URI.create(imageUrl),
        //     HttpMethod.GET,
        //     null,
        //     byte[].class);

        //     byte[] imageBody = response.getBody();

        //     File tempImage= new File("temp_image.jpg");

        //     try(FileOutputStream outputStream = new FileOutputStream(tempImage)){
        //         outputStream.write(imageBody);
        //     }
        //     return tempImage;
    }

    private File CompressImage(BufferedImage image, float compressQuality) throws IOException{
        //TODO do something about naming convention
        String outputFileName = "outputImage_"+System.currentTimeMillis()+".jpg";
        File compressedImageFile = new File(UPLOAD_DIR,outputFileName);
       java.util.Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
      if (!writers.hasNext()) {
            throw new IllegalStateException("No writers found for JPG format.");
        }
        ImageWriter writer = writers.next();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(compressedImageFile)){
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(compressQuality);
            }
            writer.write(null,new IIOImage(image, null, null),param);
        }finally {
            writer.dispose();
        }

        return compressedImageFile;
        

    }

    private Path saveImagePath(byte[] imageBytes, String fileName) throws IOException{
         if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("Image bytes cannot be null or empty.");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }

        Path destinationFile = Paths.get(UPLOAD_DIR, fileName);
        Files.write(destinationFile, imageBytes);
        return destinationFile;
    }

}
