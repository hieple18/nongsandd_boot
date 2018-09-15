package com.nongsandd.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nongsandd.constant.Constant;
import com.nongsandd.entity.Address;
import com.nongsandd.entity.Hamlet;
import com.nongsandd.entity.ImgLink;
import com.nongsandd.entity.Mining;
import com.nongsandd.entity.Sale;
import com.nongsandd.entity.Trader;
import com.nongsandd.entity.TradingData;
import com.nongsandd.entity.User;
import com.nongsandd.model.PieChart;
import com.nongsandd.repository.LinkRepository;
import com.nongsandd.repository.MiningRepository;
import com.nongsandd.repository.SaleRepository;
import com.nongsandd.repository.TradingAgriRepository;
import com.nongsandd.repository.TradingDataRepository;

/**
 * @author: HiepLe
 * @version: Mar 17, 2018
 */
@Service
public class SaleService {
	
	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private LinkRepository linkRepository;
	
	@Autowired
    private AddressService addressService;
	
	@Autowired
	private TraderService traderService;
	
	@Autowired
    private TradingDataRepository dataRepository;
	
	@Autowired
    private TradingAgriRepository tradingAgriRepository;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private TwilloService twilloService;
	
	@Autowired
	private MiningRepository miningRepository;
	
	@Autowired
	private MiningService miningService;
	
	public Address addAddress(Sale sale, int hamletID){
		double lng = sale.getAddress().getLng();
		double lat = sale.getAddress().getLat();
		
		Address address = new Address(lat, lng, new Hamlet());
		addressService.createAddress(address, hamletID);
		
		return address;
	}
	
	public void addLinks(Sale sale, List<String> links){
		ImgLink imgLink;
		for (String link : links) {
			String[] parts = link.split(",");
			try{
				imgLink = new ImgLink(parts[0], parts[1], sale);
				linkRepository.save(imgLink);
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println(e + link);
			}
		}
	}
	
	public void delLinks(int saleID, List<String> links){
		for (String link : links) {
			linkRepository.delLink(link, saleID);
		}
	}
	
	public boolean checkToCreateSale(int userID){
		int month = Constant.CURRENT_MONTH();
		int count = saleRepository.countSalePerMonth(userID, month);
		return (count < Constant.SALE_LIMIT) ? true : false;
	}
	
	public void createSale(Sale sale, List<String> links, int hamletID, User user){
		sale.setUser(user);
		
		Address address = addAddress(sale, hamletID);
		sale.setAddress(address);
		int communeID = addressService.getCommuneIDByHamletID(hamletID);
		sale.setCommuneID(communeID);
		
		sale.setDateCreate(Constant.CURRENT_DATE());
		sale.setStatus(Constant.ENABLE_STATE);
		saleRepository.save(sale);
		
		addLinks(sale, links);
	}
	
	public void deleteSale(int saleID, User user){
		// update sale status
		Sale sale = saleRepository.getOne(saleID);
		if(sale.getUser().getId() == user.getId() && sale.getStatus() != 2){ // not in case someone mess up db
			sale.setStatus(Constant.DELETE_STATE);
			saleRepository.save(sale);
		}
	}
	
	public void deleteSaleSelected(int dataID, int traderID){
		TradingData data = dataRepository.getOne(dataID);
		dataRepository.deleteById(dataID);
		
		Trader traderToConfirm = data.getTrader();
		if(traderToConfirm.getId() == traderID){
			int saleID = data.getSale().getId();
			saleRepository.updateState(Constant.DISABLE_STATE, saleID);
			User user = saleRepository.getUser(saleID);
		
			userService.createNotification(saleID, Constant.U_SALE_REMOVE, user, traderToConfirm);
		}
		
	}
	
	public void createNotifySaleUpdate(Sale sale, User user){
		if(sale.getStatus() == Constant.SELECTED_STATE){
			Trader trader = dataRepository.getTraderSelected(sale.getId(), Constant.SELECTED_STATE);
			traderService.createNotification(sale.getId(), Constant.T_UPDATE_SELECTED, user, trader);
		}else{
			List<Trader> traders = dataRepository.getListTraderRequest(sale.getId(), Constant.ENABLE_STATE);
			for (Trader trader : traders) {
				traderService.createNotification(sale.getId(), Constant.T_UPDATE_REQUEST, user, trader);
			}
		}
	}
	
	public void updateSale(User user, Sale sale, List<String> addLinks, List<String> delLinks, int hamletID){
		User confirmUser = saleRepository.getUser(sale.getId());
		
		if(user.getId() == confirmUser.getId()){ // not in case someone mess up db
			Address address = sale.getAddress();
			addressService.createAddress(address, hamletID);
			
			sale.setUser(user);
			saleRepository.save(sale);
			
			if(delLinks != null){
				delLinks(sale.getId(), delLinks);}
			if(addLinks != null){
				addLinks(sale, addLinks);}
			
			createNotifySaleUpdate(sale, user);
		}
	}
	
	public List<Sale> getListSaleByUser(int userID){
		return saleRepository.getListSaleByUser(userID);
	}
	
	
	public List<String> getLinkSale(int saleID){
		return linkRepository.getLinkSale(saleID);
	}
	
	public List<ImgLink> getImgLink(int saleID){
		return linkRepository.getImgLink(saleID);
	}
	
	public Sale getSale(int saleID){
		return saleRepository.getOne(saleID);
	}
	
	public List<Sale> getSaleListByTrader(int traderID){
		List<Integer> tradingAgri = tradingAgriRepository.getTradingAgriByTrader(traderID);
		List<Integer> tradingSeen = dataRepository.getSaleIDByTrader(traderID, Constant.ENABLE_STATE);
		if(tradingSeen.size() > 0){
			return saleRepository.getListSaleByTrader(tradingAgri, tradingSeen);
		}else{
			return saleRepository.getListSaleByTrader1(tradingAgri);
		}
	}
	
	public List<Integer> getTradingAgriID(int traderID){
		return tradingAgriRepository.getTradingAgriByTrader(traderID);
	}
	
	public List<String> getTradingAgriName(int traderID){
		return tradingAgriRepository.getTradingAgriName(traderID);
	}
	
	public List<Object[]> getSaleRequestByTrader(int traderID){
		return dataRepository.getSaleRequestByTrader(traderID, Constant.ENABLE_STATE);
	}
	
	public List<Sale> getSaleRequestForMaps(int traderID){
		return dataRepository.getSaleRequestForMaps(traderID, Constant.ENABLE_STATE);
	}
	
	public List<Object[]> getSaleSelectedByTrader(int traderID){
		return dataRepository.getSaleSelectedByTrader(traderID, Constant.SELECTED_STATE);
	}
	
	public List<Sale> getSaleSelectedForMaps(int traderID){
		return dataRepository.getSaleSelectedForMaps(traderID, Constant.SELECTED_STATE);
	}
	
	public List<Object[]> getSaleSelectedByUser(int userID){
		return dataRepository.getSaleSelectedByUser(userID, Constant.SELECTED_STATE);
	}
	
	public List<TradingData> getSaleRequest(int saleID){
		return dataRepository.getSaleRequest(saleID, Constant.ENABLE_STATE);
	}

	public List<Object[]> getAgriForChar(int traderID){
		return dataRepository.getAgriForChar(traderID);
	}
	
	public List<Object[]> getCommuneForChar(int traderID){
		List<Object[]> communes = dataRepository.getCommuneForChar(traderID);
		
		for (Object[] objects : communes) {
			objects[0] = Constant.GET_COMMUNE_SHORT_NAME((int)objects[0]);
		}
		
		return communes;
	}
	
	public List<PieChart> getAreaForChar(int traderID){
		Mining mining = miningRepository.getMiningByTrader(traderID);
		List<PieChart> result = new ArrayList<>();
		HashMap<Integer, Integer> map = mining.getArea();
		
		/*List<Mining> minings = miningRepository.getAllMiningByTrader(traderID);
		List<PieChart> result = new ArrayList<>();
		HashMap<Integer, Integer> sum = minings.get(0).getArea();
		minings.remove(0);*/
		
		/*for (Mining mining : minings) {
			HashMap<Integer, Integer> area = mining.getArea();
			area.forEach((k,v) -> {
				int value = sum.containsKey(k)? sum.get(k) + v : v;
				sum.put(k, value);
			});
		}*/
		
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			int kk = entry.getKey()+1;
			String name = entry.getKey().toString() + "-" + kk;
			
			result.add(new PieChart(name,entry.getValue()));
		}
		
		return result;
	}
	
	public void createSaleRequest(Trader trader, int saleID){
		TradingData tradingData = new TradingData(trader, new Sale(saleID), Constant.CURRENT_DATE(),
				null, 1);
		dataRepository.save(tradingData);
		
		saleRepository.RequestCountUp(saleID);
		
		User user = saleRepository.getUser(saleID);
		userService.createNotification(saleID, Constant.U_SALE_REQUEST, user, trader);
	
		miningService.updateMining(trader.getId(), saleID);
	}
	
	public void createSaleRequestForMining(int traderID, int saleID){
		TradingData tradingData = new TradingData(new Trader(traderID), new Sale(saleID), Constant.CURRENT_DATE(),
				null, 1);
		dataRepository.save(tradingData);
		
		saleRepository.RequestCountUp(saleID);
	}
	
	public void cancelSaleRequest(int dataID){
		dataRepository.updateState(dataID, Constant.DISABLE_STATE);
		
		Sale sale = dataRepository.getSale(dataID);
		
		int count = sale.getRequestCount() - 1;
		sale.setRequestCount(count);
		if(count == 0){
			sale.setStatus(Constant.ENABLE_STATE);
		}
		saleRepository.save(sale);
	}
	
	public int confirmRequest(int requestID, User user){
		int saleID = dataRepository.getSaleID(requestID);
		saleRepository.updateState(Constant.SELECTED_STATE, saleID);
		
		dataRepository.updateStateBySaleID(saleID, Constant.DISABLE_STATE);
		
		TradingData tradingData = dataRepository.getOne(requestID);
		tradingData.setDateSelected(Constant.CURRENT_DATE());
		tradingData.setStatus(Constant.SELECTED_STATE);
		dataRepository.save(tradingData);

		String traderPhone = dataRepository.getTraderPhone(requestID);
		String content = Constant.GET_TRADER_NOTIF_CONTENT(Constant.T_CONFIRM_REQUEST, user.getName());
		twilloService.sendMessage(content, traderPhone);
		
		traderService.createNotification(requestID, Constant.T_CONFIRM_REQUEST, user, tradingData.getTrader());
		
		return saleID;
	}
	
	public void createNotifyRestore(int saleID, User user){
		List<Trader> traders = dataRepository.getListTraderRequest(saleID, Constant.ENABLE_STATE);
		
		for (Trader trader : traders) {
			traderService.createNotification(saleID, Constant.T_SALE_RESTORE, user, trader);
		}
	}
	
	public void restoreRequest (int saleID, User user){
		dataRepository.updateStateBySaleID(saleID, Constant.ENABLE_STATE);
		saleRepository.updateState(Constant.ENABLE_STATE, saleID);
		
		createNotifyRestore(saleID, user);
	}
	
	public List<Object[]> getInfoForMaps(){
		return saleRepository.getInfoForMaps();
	}
	
	public boolean checkDeleteAgri(int id){
		int count = saleRepository.countAgriSale(id, Constant.ENABLE_STATE);
		return count > 0? true : false; 
	}
	
	public void updateSaleExpired(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -Constant.SALE_EXPERID);
		Date Experid = new Date(cal.getTime().getTime());
		saleRepository.updateSaleExpired(Experid);
	}
	
	public Address getAddress(int saleID){
		return saleRepository.getAddress(saleID);
	}
	
	public List<Sale> getSaleForExcel(){
		return saleRepository.getSaleForExcel();
	}

}
