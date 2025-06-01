/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.image_download_service.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 *
 * @author souravsharma
 */
@Data
@Entity
public class outputProductUrls {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int outputUrlId;

    private String outputUrls;

    /*
     * will be mapped to url id(primary key) of csv product urls table
     * added unique constriant to ensure no duplicate entries
     */
    
    @jakarta.persistence.Column(unique = true)
    private int inputUrlId;

    @Override
    public String toString() {
        return "outputProductUrls [outputUrlId=" + outputUrlId + ", outputUrls=" + outputUrls + ", inputUrlId="
                + inputUrlId + "]";
    }
}
