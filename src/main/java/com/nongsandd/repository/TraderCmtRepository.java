package com.nongsandd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nongsandd.entity.TraderCmt;

/**
 * @author: HiepLe
 * @version: Apr 17, 2018
 */
public interface TraderCmtRepository extends JpaRepository<TraderCmt, Integer>{
	@Query("select c from TraderCmt c where c.trader.id = ?1")
	public List<TraderCmt> getCmt(int traderID);
	
	@Query("select c.id from UserCmt c where c.user.id = ?1 and c.trader.id = ?2")
	public List<Integer> getIDCmtToShowEdit(int userID, int TraderID);
	
	@Query("select count(c) from TraderCmt c where c.trader.id = ?1")
	public int getCountCmt(int traderID);
	
	@Query("select t.user.id from TraderCmt t where t.id = ?1")
    public int getUserID(int id);
	
	@Query("select t.ratingCount from TraderCmt t where t.id = ?1")
    public int getRatingStar(int id);
	
	@Query("select avg(t.ratingCount) from TraderCmt t where t.trader.id = ?1")
    public int getAvgStar(int id);
	
	@Query("select t.trader.id from TraderCmt t where t.id = ?1")
    public int getTraderID(int cmtID);
}
