package com.duft.csv_handeling.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class outputProductUrls {

    @Id
    private int outputUrlId;

    private String outputUrls;

    /*
     * will be mapped to url id(primary key) of csv product urls table
     * TODO: mapping need to be done
     */
    private int inputUrlId;

    @Override
    public String toString() {
        return "outputProductUrls [outputUrlId=" + outputUrlId + ", outputUrls=" + outputUrls + ", inputUrlId="
                + inputUrlId + "]";
    }

    public int getOutputUrlId() {
        return outputUrlId;
    }

    public String getOutputUrls() {
        return outputUrls;
    }

    public int getInputUrlId() {
        return inputUrlId;
    }

    public void setOutputUrlId(int outputUrlId) {
        this.outputUrlId = outputUrlId;
    }

    public void setOutputUrls(String outputUrls) {
        this.outputUrls = outputUrls;
    }

    public void setInputUrlId(int inputUrlId) {
        this.inputUrlId = inputUrlId;
    }
}
