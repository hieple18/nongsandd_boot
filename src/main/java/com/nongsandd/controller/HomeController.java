package com.nongsandd.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nongsandd.entity.Address;
import com.nongsandd.entity.AgriCategory;
import com.nongsandd.entity.Agriculture;
import com.nongsandd.entity.Trader;
import com.nongsandd.entity.TraderCmt;
import com.nongsandd.model.PriceRecord;
import com.nongsandd.service.AddressService;
import com.nongsandd.service.AdminService;
import com.nongsandd.service.AgricultureService;
import com.nongsandd.service.SaleService;
import com.nongsandd.service.TraderService;

/**
*@author HiepLe
*@version 1.0 Sep 24, 2017
*/

@Controller
public class HomeController {
    
    @Autowired
    private AgricultureService agricultureService;
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
	private TraderService traderService;

	@Autowired
	private SaleService saleService;
	
	@Autowired 
	private AdminService adminService;
    
    @RequestMapping(value={"/", ""})
    public String welcome(ModelMap m){
        return "redirect:/gia/hom-nay"; 
    }
    
    @RequestMapping(value="/dang-nhap")
    public String login(@RequestParam(value = "error", required = false) String error, 
    	      ModelMap model){
    	if (error != null) {
    	      model.addAttribute("message", "Đăng nhập thất bại!");
    	    }
    	return "login";
    }
    
    @RequestMapping(value="/dang-xuat", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response, ModelMap m) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        
        return "redirect:/";
    }
    
    // handle price request
    @RequestMapping(value = "/create-random-price")
    @ResponseBody
    public boolean createRandomPrice() throws IOException {
        agricultureService.randomPrice();
        return true;
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
       
        m.addAttribute("role", "all");
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
    	
    	m.addAttribute("page", page);
        return "price-chart";
    }
    
    // get list price by agricultureID to compare
    @RequestMapping(value="/gia/so-sanh", method = RequestMethod.GET)
    @ResponseBody
    public List<Float> getListPrice(@RequestParam("id") int id){
    	List<Float> agriPrices = agricultureService.getPriceChart(id);
        return agriPrices;
    }
    
    @RequestMapping(value = "/thon", method = RequestMethod.GET)
    @ResponseBody
    public List<Object[]> GetThonBylstmaxa(@RequestParam("lstmaxa") int lstmaxa, ModelMap m) {
        List<Object[]> hamlets = addressService.getHamletNameByCommuneID(lstmaxa); 
        return hamlets;
    }
    
    //end handle price request
    /////////////////////////////////////////////////////
    
    @RequestMapping(value = "/nha-buon/thong-tin", method = RequestMethod.GET)
	public String getTraderInfo(@RequestParam("id") int traderID, ModelMap m) {
		Trader trader = traderService.getTrader(traderID);
		m.addAttribute("trader", trader);

		List<TraderCmt> cmts = traderService.getTraderCmts(traderID);
		m.addAttribute("cmts", cmts);
		m.addAttribute("cmtSize", cmts.size());

		List<String> tradingAgris = saleService.getTradingAgriName(traderID);
		m.addAttribute("tradingAgris", tradingAgris);

		Address address = traderService.getAddress(traderID);
		m.addAttribute("address", address);

		return "trader-profile";
	}
    
    @RequestMapping(value = "/nha-buon/danh-sach", method = RequestMethod.GET)
	public String getListTrader(ModelMap m) {

		List<Trader> traders = traderService.getListTrader();
		m.addAttribute("traders", traders);
		return "list-trader";
	}
    
    @RequestMapping(value = "/create-admin-332284")
	public String createAdmin() {

    	adminService.createAdmin();
		return "redirect:/admin";
	}
}
