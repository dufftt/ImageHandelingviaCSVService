/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.duft.image_download_service.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duft.image_download_service.Entities.csvProductUrls;

/**
 *
 * @author souravsharma
 */
@Repository
public interface CsvProductUrlsRepositry extends JpaRepository<csvProductUrls, Integer> {

    List<csvProductUrls> findByProcessedIndFalse();

    List<csvProductUrls> findByProcessedIndTrue();

}
