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
import com.nongsandd.model.PriceList;
import com.nongsandd.model.PriceU;
import com.nongsandd.service.AgricultureService;

/**
 * @author: HiepLe
 * @version: May 5, 2018
 */

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
    private AgricultureService agricultureService;

	@RequestMapping("/")
	public String index(){
		return "redirect:/admin/agri";
	}
	
	@RequestMapping("/agri")
	public String getAgri(ModelMap m){
		List<Agriculture> agricultures = agricultureService.getAllAgri();
		m.addAttribute("agris", agricultures);
		
		List<AgriCategory> agriCategories = agricultureService.getAllAgriCategory();
		m.addAttribute("agriCategories", agriCategories);
		
		return "admin/agricultures";
	}
	
	@RequestMapping("/add-agri")
	public String addAgri(@RequestParam("categoryID") int cID,
			@RequestParam("name") String name){
		
		agricultureService.AddAgri(cID, name);
		return "redirect:/admin/agri";
	}
	
	@RequestMapping("/update-agri")
	public String updateAgri(@RequestParam("categoryID") int cID, @RequestParam("id") int id,
			@RequestParam("name") String name){
		
		agricultureService.updateAgri(id, cID, name);
		return "redirect:/admin/agri";
	}
	
	@RequestMapping("/price-add")
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
	
	@RequestMapping("/create-price")
	public String createPrice(@ModelAttribute("price") PriceList list){
		
		agricultureService.createPrice(list);
		return "redirect:/admin/price-update";
	}
	
	@RequestMapping("/price-update")
	public String updatePrice(ModelMap m){
		PriceU priceList = agricultureService.getPriceUpdate();
		m.addAttribute("priceList", priceList);
		
		return "admin/price-update";
	}
	
	@RequestMapping("/save-update-price")
	public String saveUpdatePrice(@ModelAttribute("priceList") PriceU list){
		agricultureService.saveUpdatePrice(list);
		
		return "redirect:/admin/agri";
	}
	
	@RequestMapping("/check-delete-agri")
	@ResponseBody
	public boolean checkDeleteAgri(@RequestParam("id") int id){
		/*return saleService.checkDeleteAgri(id);*/
		return true;
	}
	
	@RequestMapping("/delete-agri")
	public String deleteAgri(@RequestParam("id") int id){
		agricultureService.deleteAgri(id);
		
		return "redirect:/admin/agri";
	}
	
	@RequestMapping("/acreate-category")
	public String autoCreateCategory(@RequestParam("id") int id){
		agricultureService.autoAddCategory();
		
		return "redirect:/admin/agri";
	}
	
}