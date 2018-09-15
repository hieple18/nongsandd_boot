package com.nongsandd.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nongsandd.entity.Address;
import com.nongsandd.entity.AgriCategory;
import com.nongsandd.entity.Agriculture;
import com.nongsandd.entity.Commune;
import com.nongsandd.entity.Hamlet;
import com.nongsandd.entity.Sale;
import com.nongsandd.entity.Trader;
import com.nongsandd.entity.TraderCmt;
import com.nongsandd.entity.TradingData;
import com.nongsandd.entity.User;
import com.nongsandd.entity.UserCmt;
import com.nongsandd.entity.UserNotification;
import com.nongsandd.model.PriceRecord;
import com.nongsandd.repository.UserRepository;
import com.nongsandd.service.AddressService;
import com.nongsandd.service.AgricultureService;
import com.nongsandd.service.SaleService;
import com.nongsandd.service.TraderService;
import com.nongsandd.service.UserService;

/**
 * @author: HiepLe
 * @version: Sep 1, 2018
 */

@Controller
@RequestMapping("/NguoiDung")
public class UserController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TraderService traderService;
	
	@Autowired
	private SaleService saleService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private AgricultureService agricultureService;

	public int getUserID() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		return userService.getUserIdByPhoneNum(name);
	}

	public User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		return userService.getUserByPhoneNum(name);
	}
	
	public String getName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		return userService.getNameByPhoneNum(name);
	}

	public List<UserNotification> getNotify() {
		return userService.getNotify(getUserID());
	}
	
	@RequestMapping(value = { "", "/" })
	public String home(ModelMap m) {
		List<Sale> sales = saleService.getListSaleByUser(getUserID());
		m.addAttribute("sales", sales);

		m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
		return "user/index";
	}
	
	// get price
    @RequestMapping(value="/gia/{date}", method = RequestMethod.GET)
    public String getPriceListByDate(@PathVariable("date") String date, ModelMap m){
    	List<PriceRecord> agriPrices;
    	String title;
    	if(date.equals("hom-nay")){
    		agriPrices = agricultureService.getPriceToday();
    		title = "Hôm Nay";
    	}else{
    		agriPrices = agricultureService.getPriceByday(date);
    		title = "Ngày " + date.toString();
    	}
        m.addAttribute("agriPrices", agriPrices);
        m.addAttribute("title", title);
        
        Date minDate = agricultureService.getMinDatePrice();
        m.addAttribute("minDate", minDate);
       
        m.addAttribute("role", "user");
        m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
        return "price-list";
    }
    
    // price chart
    @RequestMapping(value="/gia/bieu-do", method = RequestMethod.GET)
    public String getPriceChart(@RequestParam("id") int id,
    		@RequestParam("page") int page, ModelMap m){
    	List<Float> agriPrices = agricultureService.getPriceChart(id);
    	m.addAttribute("agriPrices", agriPrices);
    	
    	List<Agriculture> agricultures = agricultureService.getAllAgri();
		m.addAttribute("agris", agricultures);
		
		List<AgriCategory> agriCategories = agricultureService.getAllAgriCategory();
		m.addAttribute("agriCategories", agriCategories);
    	
    	String currAgriName = agricultures.get(id - 1).getName();
    	agricultures.remove(id - 1);
    	m.addAttribute("currAgriName", currAgriName);
    	
    	m.addAttribute("role", "user");
    	m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
        return "price-chart";
    }

	@RequestMapping(value = "/tai-khoan", method = RequestMethod.GET)
	public String about(ModelMap m) {
		User user = getUser();
		m.addAttribute("user", user);
		
		List<UserCmt> cmts = userService.getUserCmts(user.getId());
		m.addAttribute("cmts", cmts);
		m.addAttribute("cmtSize", cmts.size());
		
		Address address = userRepository.getAddress(user.getId());
		m.addAttribute("address", address);

		m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
		return "user/my-profile";
	}
	
	@RequestMapping(value = "/tai-khoan/chinh-sua", method = RequestMethod.GET)
	public String getUpdatePage(ModelMap m) {
		User user = getUser();
		m.addAttribute("user", user);
		
		Address address = addressService.getAddressByID(user.getId());
		m.addAttribute("address", address);

		List<Commune> communes = addressService.getCommuneByDistrictID(677);
		m.addAttribute("communes", communes);

		int communeID = addressService.getCommuneIDByHamletID(address.getHamlet().getHamletID());
		m.addAttribute("communeID", communeID);

		List<Hamlet> hamlets = addressService.getHamletByCommnune(communeID);
		m.addAttribute("hamlets", hamlets);
		
		m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
		return "user/profile-update";
	}
	
	@RequestMapping(value = "/tai-khoan/cap-nhap", method = RequestMethod.PUT)
	public String updateUser(@ModelAttribute("user") User user, @RequestParam(value = "hamletID") int hamletID) {
		User oldUser = getUser();
		userService.updateUser(user, oldUser, hamletID);
		return "redirect:/NguoiDung/thong-tin-tai-khoan";
	}
	
	// end handle my profile request
	//////////////////////////////////////////////////
	// handle trader profile request
	
	@RequestMapping(value = "/nha-buon/thong-tin", method = RequestMethod.GET)
	public String getUserInfo(@RequestParam("id") int traderID, ModelMap m) {
		Trader trader = traderService.getTrader(traderID);
		m.addAttribute("trader", trader);
		
		List<Integer> editCmts = traderService.getIDCmtToShowEdit(getUserID(), traderID);
		m.addAttribute("editCmts", editCmts);
		
		List<TraderCmt> cmts = traderService.getTraderCmts(traderID);
		m.addAttribute("cmts", cmts);
		m.addAttribute("cmtSize", cmts.size());
		
		Address address = traderService.getAddress(traderID);
		m.addAttribute("address", address);
		
		List<String> tradingAgris = saleService.getTradingAgriName(traderID);
		m.addAttribute("tradingAgris", tradingAgris);
		
		m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
		return "user/trader-profile";
	}

	@RequestMapping(value = "/cmt/kiem-tra", method = RequestMethod.GET)
	@ResponseBody
	public boolean checkCmt(@RequestParam("id") int traderID, ModelMap m) {

		return userService.checkConditionToAddCmt(getUserID(), traderID);
	}

	@RequestMapping(value = "/cmt/them", method = RequestMethod.POST)
	public String addUserCmt(@RequestParam("id") int traderID, @RequestParam("ratingCount") int ratingCount,
			@RequestParam("cmtContent") String content) {
		traderService.CreateCmtAboutTrader(getUser(), traderID, ratingCount, content);
		String redirect = "redirect:/NguoiDung/thong-tin-nb?id=" + traderID;
		return redirect;
	}
	
	@RequestMapping(value = "/cmt/cap-nhap", method = RequestMethod.PUT)
	public String editUserCmt(@RequestParam("traderID") int traderID, @RequestParam("ratingCount") int ratingCount,
			@RequestParam("cmtContent") String content, @RequestParam("cmtID") int cmtID) {
		traderService.updateCmtAboutTrader(cmtID, getUser(), traderID, ratingCount, content);
		String redirect = "redirect:/NguoiDung/thong-tin-nb?id=" + traderID;
		return redirect;
	}
	
	@RequestMapping(value = "/cmd/xoa", method = RequestMethod.DELETE)
	public String deleteUserCmt(@RequestParam("id") int id) {
		traderService.deleteCmtAboutTrader(getUser(), id);
		return "redirect:/NguoiDung/";
	}
	
	// end handle trader profile request
	/////////////////////////////////////////////////////
	// handle sale request

	@RequestMapping(value = "/yeu-cau/ds")
	public String getSaleRequest(@RequestParam(value = "id") int saleID, ModelMap m) {
		List<TradingData> requests = saleService.getSaleRequest(saleID);
		m.addAttribute("requests", requests);

		m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
		return "user/sale-request";
	}

	@RequestMapping(value = "/yeu-cau/xac-nhan")
	public String confirmSaleRequest(@RequestParam(value = "id") int requestID, ModelMap m) {
		saleService.confirmRequest(requestID, getUser());
		return "redirect:/NguoiDung/ds-tin-ban";
	}
	
	@RequestMapping(value = "/thong-bao/da-xem")
	@ResponseBody
	public boolean disableNotification(@ModelAttribute("notifyID") int notifyID) {

		userService.disableNotification(notifyID);
		return true;
	}
}
