package com.nongsandd.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nongsandd.entity.AgriCategory;
import com.nongsandd.entity.Agriculture;
import com.nongsandd.model.PriceRecord;
import com.nongsandd.service.AgricultureService;

/**
 * @author: HiepLe
 * @version: Jul 26, 2018
 */

@Controller
public class HomeController {

	@Autowired
    private AgricultureService agricultureService;
	
	@RequestMapping("/")
	public String index(ModelMap m){
		List<PriceRecord> agriPrices = agricultureService.getPriceToday();
        m.addAttribute("agriPrices", agriPrices); 
        
        m.addAttribute("title", "Hôm Nay");
		return "price-list";
	}
	
	@RequestMapping(value = "/create-random-price")
    @ResponseBody
    public boolean createRandomPrice() throws IOException {
        agricultureService.randomPrice();
        return true;
    }
	
	// price chart
    @RequestMapping(value="/bieu-do-gia")
    public String getPriceChart(@RequestParam("id") int id, ModelMap m){
    	List<Float> agriPrices = agricultureService.getPriceChart(id);
    	m.addAttribute("agriPrices", agriPrices);
    	
    	List<Agriculture> agricultures = agricultureService.getAllAgri();
		m.addAttribute("agris", agricultures);
		
		List<AgriCategory> agriCategories = agricultureService.getAllAgriCategory();
		m.addAttribute("agriCategories", agriCategories);
    	
    	String currAgriName = agricultures.get(id - 1).getName();
    	agricultures.remove(id - 1);
    	m.addAttribute("currAgriName", currAgriName);

        return "price-chart";
    }
    
 // get list price by agricultureID to compare
    @RequestMapping(value="/lay-ds-gia")
    @ResponseBody
    public List<Float> getListPrice(@RequestParam("id") int id){
    	List<Float> agriPrices = agricultureService.getPriceChart(id);
        return agriPrices;
    }
}
