/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.duft.csv_handeling.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duft.csv_handeling.Entity.csvProduct;
import com.duft.csv_handeling.Entity.outputProductUrls;

/**
 *
 * @author souravsharma
 */
@Repository
public interface OutputUrlRepository extends JpaRepository<outputProductUrls, Integer> {

List<outputProductUrls> findByInputUrlId(Integer inputUrlId); // Corrected method name}

}
