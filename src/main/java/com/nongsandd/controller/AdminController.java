package com.nongsandd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nongsandd.entity.AgriCategory;
import com.nongsandd.entity.Agriculture;
import com.nongsandd.entity.Trader;
import com.nongsandd.entity.User;
import com.nongsandd.model.PriceList;
import com.nongsandd.model.PriceU;
import com.nongsandd.service.AgricultureService;
import com.nongsandd.service.SaleService;
import com.nongsandd.service.TraderService;
import com.nongsandd.service.UserService;

/**
 * @author: HiepLe
 * @version: May 5, 2018
 */

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private SaleService saleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TraderService traderService;
	
	@Autowired
    private AgricultureService agricultureService;

	@RequestMapping("")
	public String home(ModelMap m){
		List<Object[]> sales = saleService.getInfoForMaps();
		m.addAttribute("sales", sales);
		
		List<Object[]> users = userService.getInfoForMaps();
		m.addAttribute("users", users);
		
		List<Object[]> traders = traderService.getInfoForMaps();
		m.addAttribute("traders", traders);
		return "admin/main";
	}
	
	@RequestMapping("/agri")
	public String getAgri(ModelMap m){
		List<Agriculture> agricultures = agricultureService.getAllAgri();
		m.addAttribute("agris", agricultures);
		
		List<AgriCategory> agriCategories = agricultureService.getAllAgriCategory();
		m.addAttribute("agriCategories", agriCategories);
		
		return "admin/agricultures";
	}
	
	@RequestMapping("/agri/add")
	public String addAgri(@RequestParam("categoryID") int cID,
			@RequestParam("name") String name){
		
		agricultureService.AddAgri(cID, name);
		return "redirect:/admin/agri";
	}
	
	@RequestMapping("/agri/update")
	public String updateAgri(@RequestParam("categoryID") int cID, @RequestParam("id") int id,
			@RequestParam("name") String name){
		
		agricultureService.updateAgri(id, cID, name);
		return "redirect:/admin/agri";
	}
	
	@RequestMapping("/price/add")
	public String addPrice(ModelMap m){
		if(agricultureService.checkExistsPriceToday()){
			return "redirect:/admin/price-update";
		}
		
		List<Agriculture> agricultures = agricultureService.getAllAgri();
		m.addAttribute("agricultures", agricultures);
		
		PriceList priceList = agricultureService.convertToPriceR(agricultures);
		m.addAttribute("priceList", priceList);
		
		return "admin/price-add";
	}
	
	@RequestMapping("/price/create")
	public String createPrice(@ModelAttribute("price") PriceList list){
		
		agricultureService.createPrice(list);
		return "redirect:/admin/price-update";
	}
	
	@RequestMapping("/price/update")
	public String updatePrice(ModelMap m){
		PriceU priceList = agricultureService.getPriceUpdate();
		m.addAttribute("priceList", priceList);
		
		return "admin/price-update";
	}
	
	@RequestMapping("/price/save")
	public String saveUpdatePrice(@ModelAttribute("priceList") PriceU list){
		agricultureService.saveUpdatePrice(list);
		
		return "redirect:/admin/agri";
	}
	
	@RequestMapping("/agri/delete/check")
	@ResponseBody
	public boolean checkDeleteAgri(@RequestParam("id") int id){
		return saleService.checkDeleteAgri(id);
	}
	
	@RequestMapping("/agri/delete")
	public String deleteAgri(@RequestParam("id") int id){
		agricultureService.deleteAgri(id);
		
		return "redirect:/admin/agri";
	}
	
	@RequestMapping("/user/list")
	public String listUser(ModelMap m){
		
		List<User> users = userService.getListUser();
		m.addAttribute("users", users);
		
		return "admin/list-user";
	}
	
	@RequestMapping("/user/info")
	public String userInfo(@RequestParam("id") int id, ModelMap m){
		
		List<User> users = userService.getListUser();
		m.addAttribute("users", users);
		
		return "admin/list-user";
	}
	
	@RequestMapping("/trader/list")
	public String listTrader(ModelMap m){
		
		List<User> users = userService.getListUser();
		m.addAttribute("users", users);
		
		return "admin/list-trader";
	}
	
	@RequestMapping("/trader/info")
	public String traderInfo(@RequestParam("id") int id, ModelMap m){
		
		List<User> users = userService.getListUser();
		m.addAttribute("users", users);
		
		return "admin/list-user";
	}
	
	@RequestMapping("/sale/list")
	public String listSale(ModelMap m){
		
		List<User> users = userService.getListUser();
		m.addAttribute("users", users);
		
		return "admin/sale-list";
	}
	
	@RequestMapping("/sale/info")
	public String saleInfo(@RequestParam("id") int id, ModelMap m){
		
		List<User> users = userService.getListUser();
		m.addAttribute("users", users);
		
		return "admin/list-user";
	}
	
	@RequestMapping("/sale/expired")
	public String updateSaleExpired(){
		
		saleService.updateSaleExpired();
		return "redirect:/admin";
	}
	
	@RequestMapping("/trader/phone/add")
	public String addTraderPhone(){
		return "admin/add-trader-phone";
	}
	
	@RequestMapping("/trader/phone/create")
	public String createTraderPhone(@RequestParam("phone") String phone){
		traderService.verifyTraderByAdmin(phone);
		return "redirect:/admin";
	}
	
	@RequestMapping("/trader/phone/update")
	public String updateTraderPhone(@RequestParam("phone") String phone, @RequestParam("id") int id){
		traderService.updateTraderByAdmin(id, phone);
		return "redirect:/admin/wait-to-register";
	}
	
	@RequestMapping("/trader/phone/delete")
	public String deleteTraderPhone(@RequestParam("id") int id){
		traderService.deleteTraderByAdmin(id);
		return "redirect:/admin/wait-to-register";
	}
	
	@RequestMapping("/trader/phone/list")
	public String phoneNotRegister(ModelMap m){
		List<Trader> traders = traderService.getTraderWait();
		m.addAttribute("traders", traders);
		return "admin/wait-to-register";
	}
}