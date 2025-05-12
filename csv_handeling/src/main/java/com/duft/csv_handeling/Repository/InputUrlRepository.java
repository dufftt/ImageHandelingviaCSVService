/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.duft.csv_handeling.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.duft.csv_handeling.Entity.csvProductUrls;

import jakarta.transaction.Transactional;

/**
 *
 * @author souravsharma
 */
@Repository
public interface InputUrlRepository extends JpaRepository<csvProductUrls, Integer> {

    @Modifying
    @Transactional // It's good practice to have modifying queries in a transaction
    @Query("UPDATE csvProductUrls u SET u.processedInd = :processedInd WHERE u.urlID = :urlID")
    int updateProcessedIndByUrlID(@Param("urlID") int urlID, @Param("processedInd") boolean processedInd);

}
