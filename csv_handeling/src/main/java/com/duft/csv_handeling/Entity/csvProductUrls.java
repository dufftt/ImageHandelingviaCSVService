package com.duft.csv_handeling.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class csvProductUrls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int urlID;

   // private int productID;

    private String productUrl;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private csvProduct product; // Many-to-one relationship with Product

    private boolean processedInd;

    // @Version
    // private int version; // Optimistic locking field

    // @Override
    // public String toString() {
    //     return "csvProductUrls [urlID=" + urlID + ", productUrl=" + productUrl + ", product=" + product
    //             + ", processedInd=" + processedInd + "]";
    // }

    public int getUrlID() {
        return urlID;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public csvProduct getProduct() {
        return product;
    }

    public boolean isProcessedInd() {
        return processedInd;
    }

    public void setUrlID(int urlID) {
        this.urlID = urlID;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setProduct(csvProduct product) {
        this.product = product;
    }

    public void setProcessedInd(boolean processedInd) {
        this.processedInd = processedInd;
    }
}
