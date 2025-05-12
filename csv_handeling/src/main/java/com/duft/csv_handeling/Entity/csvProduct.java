package com.duft.csv_handeling.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class csvProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productID;

    private int productSerialNumber;

    private String productName;

    private String trackID;

    // @Version
    // private int version; // Optimistic locking field
    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<csvProductUrls> productUrls; // One-to-many relationship with ProductUrl


    // @Override
    // public String toString() {
    //     return "csvProductTable [productID=" + productID + ", productSerialNumber=" + productSerialNumber
    //             + ", productName=" + productName + ", trackID=" + trackID + ", productUrls=" + productUrls + "]";
    // }

    public int getProductID() {
        return productID;
    }

    public int getProductSerialNumber() {
        return productSerialNumber;
    }

    public String getProductName() {
        return productName;
    }

    public String getTrackID() {
        return trackID;
    }

    public List<csvProductUrls> getProductUrls() {
        return productUrls;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setProductSerialNumber(int productSerialNumber) {
        this.productSerialNumber = productSerialNumber;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public void setProductUrls(List<csvProductUrls> productUrls) {
        this.productUrls = productUrls;
    }


}
