package com.nongsandd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nongsandd.entity.Account;
import com.nongsandd.entity.Address;
import com.nongsandd.entity.AgriCategory;
import com.nongsandd.entity.Agriculture;
import com.nongsandd.entity.Commune;
import com.nongsandd.entity.Hamlet;
import com.nongsandd.entity.Trader;
import com.nongsandd.service.AddressService;
import com.nongsandd.service.AgricultureService;
import com.nongsandd.service.ChangePassService;
import com.nongsandd.service.SaleService;
import com.nongsandd.service.TraderService;

/**
 * @author: HiepLe
 * @version: Sep 9, 2018
 */

@Controller
public class TempTraderController {

	@Autowired
	private AgricultureService agricultureService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private TraderService traderService;

	@Autowired
	private SaleService saleService;
	
	@Autowired
	private ChangePassService passService;

	///////////////////////////////////////////////
	// handle trader register request
	
	
	@RequestMapping(value = "/temp/sdt/xac-thuc/yeu-cau")
    public String verifyPhone(){
		return "trader/phone-verify";
    }

	@RequestMapping(value = "/temp/sdt/admin-xac-thuc")
	@ResponseBody
	public boolean checkPhoneVerify(@RequestParam("phone") String phoneNum) {
		return traderService.checkVerifyTraderByAdmin(phoneNum);
	}

	@RequestMapping(value = "/temp/tai-khoan/dang-ki")
	public String tradeRegister(@RequestParam("phone") String phoneNum, ModelMap m) {
		if(!traderService.checkVerifyTraderByAdmin(phoneNum))
			return "trader/404";
		
		List<Commune> communes = addressService.getCommuneByDistrictID(677);
		m.put("communes", communes);

		List<Agriculture> agricultures = agricultureService.getAllAgri();
		m.addAttribute("agris", agricultures);

		List<AgriCategory> agriCategories = agricultureService.getAllAgriCategory();
		m.addAttribute("agriCategories", agriCategories);

		Trader trader = new Trader();
		Address address = new Address();
		Account account = new Account();
		trader.setAddress(address);
		trader.setAccount(account);
		m.put("trader", trader);

		m.addAttribute("phone", phoneNum);
		return "trader/register";
	}

	@RequestMapping(value = "/temp/tai-khoan/tao-moi")
	public String createTrader(@ModelAttribute("trader") Trader trader, @RequestParam(value = "hamletID") int hamletID,
			@RequestParam(value = "tradingList") String tradingList, ModelMap m) {

		int traderID = passService.checkToCreateTrader(trader, hamletID, tradingList);
		if(traderID == -1) // this phone number already used
			return "404";
		
		m.addAttribute("id", traderID);
        m.addAttribute("title", "Mã xác nhận đã được gửi về SDT: " + trader.getPhoneNum());
        m.addAttribute("action", "/NongSanDD/TempNB/xac-nhan-tk");
        m.addAttribute("success", "/NongSanDD/NhaBuon");
        return "user/code-verify";
	}
	
	@RequestMapping(value = "/temp/sdt/kiem-tra")
    @ResponseBody
    public int checkTraderPhoneNum(@RequestParam("phoneNum") String phoneNum){
    	return passService.checkPhoneNum(phoneNum);
    }
	
	@RequestMapping(value = "/temp/sdt/xac-thuc")
    @ResponseBody
    public int checkVerifyTraderPhoneNum(@RequestParam("phoneNum") String phoneNum){
    	return passService.checkVerifyPhoneNum(phoneNum);
    }
	
	@RequestMapping(value="/xac-nhan-tk")
    @ResponseBody
    public boolean verifyUser(@RequestParam(value = "id") int id,
    		@RequestParam(value = "code") int code){
        boolean result = passService.verifyAccount(id, code);
        return result;
    }
	
	@RequestMapping(value="/temp/ma-xn/gui-lai")
    @ResponseBody
    public boolean resendVerify(@RequestParam(value = "id") int id){
		passService.resendVerify(id);
        return true;
    }
	// end handle trader register request
	// handle account did not active
	
	@RequestMapping(value="/kich-hoat-tk")
    public String activeAccount(@RequestParam(value = "phone") String phoneNum, ModelMap m){
        int traderID = passService.activeAccount(phoneNum);
        
        if(traderID == -1)
        	return "404";
        
        m.addAttribute("id", traderID);
        m.addAttribute("title", "Mã xác nhận đã được gửi về SDT: " + phoneNum);
        m.addAttribute("action", "/NongSanDD/TempNB/xac-nhan-tk");
        m.addAttribute("success", "/NongSanDD/NhaBuon");
        return "trader/code-verify";
    }
    
    @RequestMapping(value="/tk-chua-kh")
    public String viewAccountNotActive(@RequestParam(value = "phone") String phoneNum, ModelMap m){
    	int userID = passService.activeAccount(phoneNum);
        
        if(userID == -1)
        	return "404";
        
        m.addAttribute("id", userID);
        m.addAttribute("title", "Mã xác nhận đã được gửi về SDT: " + phoneNum + ". Để xem thông "
        		+ "tin tài khoản, vui lòng nhập mã xác nhận");
        m.addAttribute("action", "/NongSanDD/TempNB/kiem-tra-code");
        m.addAttribute("success", "/NongSanDD/TempNB/xn-tk-chua-kh?phone=" + phoneNum);
        return "trader/code-verify";
    }
    
    @RequestMapping(value="/kiem-tra-code")
    @ResponseBody
    public boolean verifyUserCode(@RequestParam(value = "id") int id,
    		@RequestParam(value = "code") int code){
        boolean result = passService.verifyTrader(id, code);
        
        return result;
    }
    
    @RequestMapping(value="/tai-khoan/chua-xn/kich-hoat")
    public String updateUAccountNotActive(@RequestParam(value = "phone") String phone, ModelMap m){
        Trader trader = passService.getTraderNotActive(phone);
        if(trader == null)
        	return "404";
        
        m.addAttribute("trader", trader);

		List<Commune> communes = addressService.getCommuneByDistrictID(677);
		m.addAttribute("communes", communes);

		List<Hamlet> hamlets = addressService.getHamletByCommnune(trader.getCommune());
		m.addAttribute("hamlets", hamlets);
		
		int hamletID = addressService.getHamletByAddressID(trader.getAddress().getId());
		m.addAttribute("hamletID", hamletID);
		
		//for trading agri list
		List<Agriculture> agricultures = agricultureService.getAllAgri();
		m.addAttribute("agris", agricultures);

		List<AgriCategory> agriCategories = agricultureService.getAllAgriCategory();
		m.addAttribute("agriCategories", agriCategories);
		
		List<Integer> tradingAgri = saleService.getTradingAgriID(trader.getId());
		m.addAttribute("tradingList", tradingAgri);

		return "profile-nb";
    }
    
    @RequestMapping(value = "/cap-nhap-tt")
	public String updateUser(@ModelAttribute("trader") Trader trader, @RequestParam(value = "hamletID") int hamletID,
			@RequestParam(value = "tradingList") String tradingList, ModelMap m) {
		int userID = passService.updateTraderNotActive(trader, hamletID, tradingList);
		if(userID == -1)
			return "404";
		
		m.addAttribute("id", userID);
        m.addAttribute("title", "Mã xác nhận đã được gửi về SDT: " + trader.getPhoneNum());
        m.addAttribute("action", "/NongSanDD/TempNB/xac-nhan-tk");
        m.addAttribute("success", "/NongSanDD/NhaBuon");
        return "user/code-verify";
	}
    ////////////////////////////////////////////////////////////

	@RequestMapping(value = "/mk/quen")
	public String passChageRequest(ModelMap m) {
		return "trader/forgot-pass";
	}
	
	@RequestMapping(value = "/mk/thay-doi")
	public String changePass(@RequestParam("phone") String phone, @RequestParam("pass") String pass, ModelMap m) {
		passService.changePass(phone, pass);
		m.addAttribute("phone", phone);
    	
    	return "trader/confirm-change-pass";
	}

	@RequestMapping(value = "/mk/thay-doi/xac-nhan")
    @ResponseBody
    public boolean confirmUChangePass(@RequestParam("phone") String phone,
    		@RequestParam("code") int code){
    	return passService.confirmChangePass(phone, code);
    }
}
