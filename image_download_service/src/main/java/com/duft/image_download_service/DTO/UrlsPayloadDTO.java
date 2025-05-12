/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.image_download_service.DTO;

import java.util.List;

import com.duft.image_download_service.Entities.csvProductUrls;
import com.duft.image_download_service.Entities.outputProductUrls;

import lombok.Data;

/**
 *
 * @author souravsharma
 */
@Data
public class UrlsPayloadDTO {

    private List<csvProductUrls> inputUrls;
    private List<outputProductUrls> outputUrls;

    public UrlsPayloadDTO(List<csvProductUrls> inputUrls,List<outputProductUrls> outputUrls){
        this.inputUrls = inputUrls;
        this.outputUrls = outputUrls;
    }

}
