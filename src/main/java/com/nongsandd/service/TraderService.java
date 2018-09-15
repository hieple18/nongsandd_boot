package com.nongsandd.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nongsandd.constant.Constant;
import com.nongsandd.entity.Address;
import com.nongsandd.entity.Agriculture;
import com.nongsandd.entity.Trader;
import com.nongsandd.entity.TraderCmt;
import com.nongsandd.entity.TraderNotification;
import com.nongsandd.entity.TradingAgri;
import com.nongsandd.entity.User;
import com.nongsandd.repository.AgricultureRepository;
import com.nongsandd.repository.TraderCmtRepository;
import com.nongsandd.repository.TraderNotifyRepository;
import com.nongsandd.repository.TraderRepository;
import com.nongsandd.repository.TradingAgriRepository;
import com.nongsandd.repository.TradingDataRepository;

/**
 * @author: HiepLe
 * @version: Mar 16, 2018
 */

@Service
public class TraderService {
	@Autowired
    private AddressService addressService;
	
	@Autowired
    private TraderRepository traderRepository; 
	
	@Autowired
	private TraderNotifyRepository notifyRepository;
	
	@Autowired
    private TradingAgriRepository tradingAgriRepository;
	
	@Autowired
    private TradingDataRepository tradingDataRepository;
	
	@Autowired
	private TraderCmtRepository traderCmtRepository;
	
	@Autowired
    private AgricultureRepository agricultureRepository;
	
	public List<Trader> getListTrader(){
		return traderRepository.findAll();
	}
	
	public void updateProfile(Trader trader, Trader oldTrader, int hamletID, String tradingList){
		trader.setId(oldTrader.getId());
		trader.setDateCreate(oldTrader.getDateCreate());
		trader.setRatingSum(oldTrader.getRatingSum());
		trader.setAccount(oldTrader.getAccount());
		
		//create address
		addressService.createAddress(trader.getAddress(), hamletID);
		
		//create trader
	    traderRepository.save(trader);
	    
	    //create trading agriculture
	    updateTradingAgri(tradingList, trader);
	}
	
	public void createNotification(int id, int status, User user, Trader trader) {
		String content = Constant.GET_TRADER_NOTIF_CONTENT(status, user.getName());
		String link = Constant.GET_TRADER_NOTIF_LINK(status, id);
		
		TraderNotification notification = new TraderNotification(trader, content, link,
				Constant.DATE_IN_MILLISECONDS(), status);
		notifyRepository.save(notification);
	}
	
	public void createTradingAgri(String tradingList, Trader trader){
		Agriculture agriculture = new Agriculture();
		
		String[] parts = tradingList.split(",");
		for (String value : parts) {
			agriculture = agricultureRepository.getOne(Integer.parseInt(value));
			TradingAgri tradingAgri = new TradingAgri(trader, agriculture);
			
			tradingAgriRepository.save(tradingAgri);
		}
	}
	
	public void updateTradingAgri(String tradingList, Trader trader){
		tradingAgriRepository.deleteByTraderID(trader.getId());
		createTradingAgri(tradingList, trader);
	}
	
	public int getTraderIdByPhoneNum(String phoneNum){
		return traderRepository.getTraderIDByPhone(phoneNum);
	}
	
	public Trader getTraderByPhoneNum(String phoneNum){
		return traderRepository.getTraderByPhone(phoneNum);
	}
	
	public String getNameByPhoneNum(String phoneNum){
		return traderRepository.getNameByPhone(phoneNum);
	}
	
	public List<TraderNotification> getNotify(int traderID) {
		List<TraderNotification> notifications = notifyRepository.getNotify(traderID);
    	for (TraderNotification notification : notifications) {
    		long previoustime = notification.getDateCreate();
			notification.setTimeAgo(getDateByMillis(previoustime));
		}
    	
    	return notifications;
	}
	
	public String getDateByMillis(long previoustime){
		long millis = Constant.DATE_IN_MILLISECONDS() - previoustime;
		long minute = (millis / (1000 * 60)) % 60;
		
		String time;
		if(minute > 0){// >1m
			long hour = (minute/60) % 24;
			if(hour > 0){ // > 1h
				time = String.format("%02dh:%02dm", hour, minute);
			}else{
				time = String.format("%02dm", minute);
			}
		}else{
			time = "1m";
		}
		return time;
	}
	
	public void disableNotification(int notifyID){
		notifyRepository.disableNotify(Constant.DISABLE_STATE, notifyID);
	}
	
	public boolean checkConditionToAddCmt(int userID, int traderID){
		Date dateSelected = tradingDataRepository.getNewestSaleSelected(userID, traderID, Constant.SELECTED_STATE);
		if(dateSelected == null){
			return false;
		}else{
			LocalDate fromDate = dateSelected.toLocalDate();
			LocalDate toDate = Constant.CURRENT_DATE().toLocalDate();
			long days = ChronoUnit.DAYS.between(fromDate, toDate);
			
			if(days > 180){
				return false;
			}
			
			return true;
		}
	}
	
	public Trader getTrader(int traderID){
		return traderRepository.getOne(traderID);
	}
	
	public void saveTrader(Trader trader){
		traderRepository.save(trader);
	}
	
	// called from user controller
	public List<TraderCmt> getTraderCmts(int traderID){
		return traderCmtRepository.getCmt(traderID);
	}
	
	// called from user controller
	public void CreateCmtAboutTrader(User user, int traderID, int ratingStar, String content) {
		addCmtAboutTrader(user, traderID, ratingStar, content);
		
		Trader trader = new Trader();
		trader.setId(traderID);
		createNotification(traderID, Constant.T_USER_CMT, user, trader);
	}

	// called from user controller
	public void addCmtAboutTrader(User user, int traderID, int ratingStar, String content) {
		float sum = traderRepository.getRatingSum(traderID);
		int count = traderCmtRepository.getCountCmt(traderID);
		float newSum = (sum + ratingStar) / (count + 1);
		
		traderRepository.updateRatingSum(newSum, traderID);
		Trader trader = traderRepository.getOne(traderID);
		
		TraderCmt cmt = new TraderCmt(trader, user, content, ratingStar, Constant.CURRENT_DATE(),
				Constant.ENABLE_STATE);
		traderCmtRepository.save(cmt);
	}
	
	// called from user controller
	public void updateCmtAboutTrader(int cmtID, User user, int traderID, int ratingStar, String content) {
		int confirmUserID = traderCmtRepository.getUserID(cmtID);
		if(user.getId() == confirmUserID){
			float sum = traderRepository.getRatingSum(traderID);
			int count = traderCmtRepository.getCountCmt(traderID);
			int previousRatingStar = traderCmtRepository.getRatingStar(cmtID);
			float newSum = (sum*count - previousRatingStar + ratingStar)/count;
			traderRepository.updateRatingSum(newSum, traderID);
			Trader trader = traderRepository.getOne(traderID);
			TraderCmt cmt = new TraderCmt(trader, user, content, ratingStar, Constant.CURRENT_DATE(),
					Constant.ENABLE_STATE);
			cmt.setId(cmtID);
			traderCmtRepository.save(cmt);
		}
	}
	
	// called from user controller
		public void deleteCmtAboutTrader(User user, int id) {
			int confirmUserID = traderCmtRepository.getUserID(id);
			if(user.getId() == confirmUserID){
				int traderID = traderCmtRepository.getTraderID(id);
				traderCmtRepository.deleteById(id);
				
				float newSum = traderCmtRepository.getAvgStar(traderID);
				traderRepository.updateRatingSum(newSum, traderID);
			}
		}
	
	// called from user controller
	public List<Integer> getIDCmtToShowEdit(int userID, int traderID){
		return traderCmtRepository.getIDCmtToShowEdit(userID, traderID);
	}
	
	public List<Object[]> getInfoForMaps(){
		return traderRepository.getInfoForMaps();
	}
	
	public Address getAddress(int traderID){
		return traderRepository.getAddress(traderID);
	}	
	
	public void verifyTraderByAdmin(String phoneNum){
		Trader trader = new Trader();
		trader.setPhoneNum(phoneNum);
		trader.setStatus(Constant.WAIT_TO_REGISTER);
		trader.setDateCreate(Constant.CURRENT_DATE());
		traderRepository.save(trader);
	}
	
	public void updateTraderByAdmin(int id, String phoneNum){
		Trader trader = traderRepository.getOne(id);
		if(trader.getStatus() == Constant.WAIT_TO_REGISTER){
			trader.setPhoneNum(phoneNum);
			traderRepository.save(trader);
		}
	}
	
	public void deleteTraderByAdmin(int id){
		Trader trader = traderRepository.getOne(id);
		if(trader.getStatus() == Constant.WAIT_TO_REGISTER){
			traderRepository.deleteById(id);
		}
	}
	
	public boolean checkVerifyTraderByAdmin(String phoneNum){
		int count = traderRepository.getTraderWaitToVerify(phoneNum, Constant.WAIT_TO_REGISTER);
		
		return (count == 0) ? false : true;
	}
	
	public List<Trader> getTraderWait(){
		return traderRepository.getTraderWaitToRegister(Constant.WAIT_TO_REGISTER);
	}
}
