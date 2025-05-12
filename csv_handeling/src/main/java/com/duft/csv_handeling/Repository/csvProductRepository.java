package com.duft.csv_handeling.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.duft.csv_handeling.Entity.csvProduct;
import com.duft.csv_handeling.Entity.csvProductUrls;

@Repository
public interface csvProductRepository extends JpaRepository<csvProduct, Integer> {

    List<csvProduct> findByTrackID(String trackID);

    
    @Query("SELECT pu FROM csvProduct p JOIN p.productUrls pu WHERE p.trackID = :trackID")
    List<csvProductUrls> findProductUrlsListByTrackID(@Param("trackID") String trackID);

    @Query("SELECT pu.urlID FROM csvProduct p JOIN p.productUrls pu WHERE p.trackID = :trackID")
    List<Integer> findProductUrlIDListByTrackID(@Param("trackID") String trackID);

    @Query("SELECT pu.processedInd FROM csvProduct p JOIN p.productUrls pu WHERE p.trackID = :trackID")
    List<Boolean> findProcessedListByTrackID(@Param("trackID") String trackID);

}
