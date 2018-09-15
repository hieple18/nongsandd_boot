package com.nongsandd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nongsandd.entity.TempAccount;;

/**
 * @author: HiepLe
 * @version: Jun 10, 2018
 */
public interface TempAccRepository extends JpaRepository<TempAccount, Integer> {
	@Query("select t from TempAccount t where t.userName = ?1")
    public TempAccount getTemp(String phone);
}
