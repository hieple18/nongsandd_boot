package com.nongsandd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nongsandd.entity.Address;
import com.nongsandd.entity.Agriculture;
import com.nongsandd.entity.Commune;
import com.nongsandd.entity.Hamlet;
import com.nongsandd.entity.ImgLink;
import com.nongsandd.entity.Sale;
import com.nongsandd.entity.User;
import com.nongsandd.entity.UserNotification;
import com.nongsandd.service.AddressService;
import com.nongsandd.service.AgricultureService;
import com.nongsandd.service.SaleService;
import com.nongsandd.service.UserService;

/**
 * @author: HiepLe
 * @version: Sep 9, 2018
 */

@Controller
@RequestMapping("/NguoiDung/tin-ban")
public class SaleUserController {
	
	@Autowired
	private AgricultureService agricultureService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private SaleService saleService;
	
	@Autowired
	private UserService userService;
	
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

	@RequestMapping(value = "/tin-ban/dang-tin")
	public String uploadSaleInfo(ModelMap m) {
		boolean creatable = saleService.checkToCreateSale(getUserID());
		m.addAttribute("creatable", creatable);
		
		Address address = addressService.getAddressByID(getUser().getAddress().getId());
		m.addAttribute("address", address);

		List<Commune> communes = addressService.getCommuneByDistrictID(677);
		m.addAttribute("communes", communes);

		int communeID = addressService.getCommuneIDByHamletID(address.getHamlet().getHamletID());
		m.addAttribute("communeID", communeID);

		List<Hamlet> hamlets = addressService.getHamletByCommnune(communeID);
		m.addAttribute("hamlets", hamlets);

		List<Agriculture> agricultures = agricultureService.getAllAgri();
		m.addAttribute("agris", agricultures);

		Sale sale = new Sale();
		sale.setAddress(new Address());
		m.addAttribute("sale", sale);
		m.addAttribute("userID", getUserID());

		m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
		return "user/upload-sale-info";
	}

	@RequestMapping(value = "/tin-ban/tao-moi")
	public String createSale(@ModelAttribute("sale") Sale sale, @RequestParam(value = "hamletID") int hamletID,
			@RequestParam(value = "links[]") List<String> links) {
		boolean creatable = saleService.checkToCreateSale(getUserID());
		if(creatable){
			saleService.createSale(sale, links, hamletID, getUser());
			return "redirect:/NguoiDung/";
		}
		
		return "trader/404";
	}
	
	@RequestMapping(value = "/tin-ban/xoa")
	public String deleteSale(@RequestParam(value = "id") int saleID){
		
		saleService.deleteSale(saleID, getUser());
		return "redirect:/NguoiDung/";
	}
	
	
	@RequestMapping(value="tin-ban/ds")
	public String getSaleSelected(ModelMap m){
		List<Object[]> datas = saleService.getSaleSelectedByUser(getUserID());
		m.addAttribute("datas", datas);

		m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
		return "user/sale-selected";
	}

	@RequestMapping(value = "/tin-ban/khoi-phuc")
	public String restoreSaleRemoved(@RequestParam(value = "id") int saleID, ModelMap m) {
		saleService.restoreRequest(saleID, getUser());

		return "redirect:/NguoiDung/ds-yeu-cau?id=" + saleID;
	}
	
	@RequestMapping(value = "/tin-ban/thon-tin")
	public String getSaleDetail(@RequestParam(value = "id") int saleID, ModelMap m) {

		Sale sale = saleService.getSale(saleID);
		m.addAttribute("sale", sale);
		m.addAttribute("userName", getUser().getName());

		List<String> links = saleService.getLinkSale(saleID);
		m.addAttribute("links", links);
		
		Address address = saleService.getAddress(saleID);
		m.addAttribute("address", address);

		m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
		return "user/sale-info";
	}
	
	@RequestMapping(value = "/tin-ban/cap-nhao")
	public String UpdateSale(@ModelAttribute("sale") Sale sale, @RequestParam(value = "hamletID") int hamletID,
			@RequestParam(value = "addlinks[]", required = false) List<String> addLinks,
			@RequestParam(value = "dellinks[]", required = false) List<String> delLinks) {

		saleService.updateSale(getUser(), sale, addLinks, delLinks, hamletID);

		return "redirect:/NguoiDung/";
	}
	
	@RequestMapping(value = "/tin-ban/hinh-anh")
	public String getLinkSale(@RequestParam(value = "saleID") int saleID, ModelMap m) {
		List<String> links = saleService.getLinkSale(saleID);
		m.addAttribute("links", links);

		m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
		return "user/img-popup";
	}
	
	@RequestMapping(value = "/tin-ban/chinh-sua")
	public String getUpdateSalePage(@RequestParam(value = "id") int saleID, ModelMap m) {
		Sale sale = saleService.getSale(saleID);
		m.addAttribute("sale", sale);

		// for address
		Address address = saleService.getAddress(saleID);
		m.addAttribute("address", address);

		List<Commune> communes = addressService.getCommuneByDistrictID(677);
		m.addAttribute("communes", communes);

		int communeID = addressService.getCommuneIDByHamletID(address.getHamlet().getHamletID());
		m.addAttribute("communeID", communeID);

		List<Hamlet> hamlets = addressService.getHamletByCommnune(communeID);
		m.addAttribute("hamlets", hamlets);

		// for link
		List<ImgLink> links = saleService.getImgLink(saleID);
		m.addAttribute("links", links);
		m.addAttribute("linksSize", links.size());

		// for agriculture
		List<Agriculture> agricultures = agricultureService.getAllAgri();
		m.addAttribute("agris", agricultures);

		m.addAttribute("notifies", getNotify());
		m.addAttribute("userName", getName());
		return "user/sale-update";
	}
}
