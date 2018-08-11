package com.nongsandd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nongsandd.entity.Agriculture;

/**
*@author HiepLe
*@version 1.0 Dec 19, 2017
*/
public interface AgricultureRepository extends JpaRepository<Agriculture, Integer> {

    @Query("select a from Agriculture a where a.agriCategory.id = ?1 and a.status = 1")
    public List<Agriculture> getAgriBySub(int subID);
    
    @Query("select a from Agriculture a where a.status = 1")
    public List<Agriculture> getAllAvailable();
}
