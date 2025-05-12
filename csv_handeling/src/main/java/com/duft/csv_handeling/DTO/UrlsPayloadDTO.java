/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.csv_handeling.DTO;

import java.util.List;

import com.duft.csv_handeling.Entity.csvProductUrls;
import com.duft.csv_handeling.Entity.outputProductUrls;

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

    public List<csvProductUrls> getInputUrls() {
        return inputUrls;
    }

    public List<outputProductUrls> getOutputUrls() {
        return outputUrls;
    }

    public void setInputUrls(List<csvProductUrls> inputUrls) {
        this.inputUrls = inputUrls;
    }

    public void setOutputUrls(List<outputProductUrls> outputUrls) {
        this.outputUrls = outputUrls;
    }

}
