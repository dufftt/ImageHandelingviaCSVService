/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.duft.image_download_service.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 *
 * @author souravsharma
 */
@Entity
@Data
public class csvProductUrls {

    @Id
    private int urlID;

   // private int productID;

    private String productUrl;

    // @JsonBackReference
    // @ManyToOne
    // @JoinColumn(name = "product_id")
    // private csvProduct product; // Many-to-one relationship with Product

    private boolean processedInd;

    // @Version
    // private int version; // Optimistic locking field

    // @Override
    // public String toString() {
    //     return "csvProductUrls [urlID=" + urlID + ", productUrl=" + productUrl + ", product=" + product
    //             + ", processedInd=" + processedInd + "]";
    // }
}

