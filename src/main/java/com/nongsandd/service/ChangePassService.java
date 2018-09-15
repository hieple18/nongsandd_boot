package com.nongsandd.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nongsandd.constant.Constant;
import com.nongsandd.entity.Account;
import com.nongsandd.entity.Mining;
import com.nongsandd.entity.TempAccount;
import com.nongsandd.entity.Trader;
import com.nongsandd.repository.AccountRepository;
import com.nongsandd.repository.MiningRepository;
import com.nongsandd.repository.TempAccRepository;
import com.nongsandd.repository.TraderRepository;
import com.nongsandd.repository.TradingAgriRepository;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

/**
 * @author: HiepLe
 * @version: Sep 6, 2018
 */
@Service
public class ChangePassService {
	@Autowired
	private AccountRepository accountrepository;
	
	@Autowired
    private AddressService addressService;
	
	@Autowired
    private TraderRepository traderRepository; 
	
	@Autowired
    private TradingAgriRepository tradingAgriRepository;

	@Autowired
    private MiningRepository miningRepository;
	
	@Autowired
	private TempAccRepository tempAccRepository;
	
	@Autowired
	private TwilloService twilloService;
	
	@Autowired
	TraderService traderService;
	
	public int createTempTrader(Trader trader, int hamletID, String tradingList, boolean initMinig){
		// create account
		String password = trader.getAccount().getPassword();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Account account = new Account(trader.getPhoneNum(), encoder.encode(password),
				Constant.ROLE_TRADER, 0);
		accountrepository.save(account);
		trader.setAccount(account);
		
		//create address
		addressService.createAddress(trader.getAddress(), hamletID);
		
		//create trader
	    trader.setDateCreate(Constant.CURRENT_DATE());
	    trader.setCode(twilloService.createVerifyCode());
		trader.setStatus(Constant.NOT_VERIFY);
	    traderRepository.save(trader);
	    
	    if(initMinig){ // if create new. then init mining for trader.
		    Mining mining = new Mining();
		    mining.setTrader(trader);
		    miningRepository.save(mining);
	    }
	    
	    //create trading agriculture
	    traderService.createTradingAgri(tradingList, trader);
	    
	    sendVerifyCode(trader.getCode(), trader.getPhoneNum());

	    return trader.getId();
	}
	
	public int checkToCreateTrader(Trader trader, int hamletID, String tradingList){
		Trader verified = traderRepository.getVerifiedTrader(trader.getPhoneNum(), Constant.WAIT_TO_REGISTER);
		if(verified != null){
			trader.setId(verified.getId());
			return createTempTrader(trader, hamletID, tradingList, true);
		}
		return -1;
	}
	
	public int updateTraderNotActive(Trader trader, int hamletID, String tradingList){
		Trader old = traderRepository.getTraderByPhone(trader.getPhoneNum());
		if(old == null || old.getStatus() != Constant.NOT_VERIFY){
			return -1;
		}
		
		trader.setId(old.getId());
		tradingAgriRepository.deleteByTraderID(trader.getId());
	    
	    return createTempTrader(trader, hamletID, tradingList, false);
	}
	
	public int activeAccount(String phoneNum){
		Trader Trader = traderRepository.getTraderByPhone(phoneNum);
		if(Trader != null){
			if(Trader.getStatus() == Constant.NOT_VERIFY){
				Trader.setCode(twilloService.createVerifyCode());
				traderRepository.save(Trader);
				sendVerifyCode(Trader.getCode(), Trader.getPhoneNum());
				return Trader.getId();
			}
		}
		
		return -1;
	}
	
	public String sendVerifyCode(int code, String phoneNum){
		String phone = "+84" + phoneNum.substring(1);
		
    	try {
            TwilioRestClient client = new TwilioRestClient(Constant.ACCOUNT_SID, Constant.AUTH_TOKEN);
     
            // Build a filter for the MessageList
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Body", "NongSanDD thong bao ma xac thuc cua ban la: " + code));
            params.add(new BasicNameValuePair("To", phone)); //Add real number here
            params.add(new BasicNameValuePair("From", Constant.TWILIO_NUMBER));
     
            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            Message message = messageFactory.create(params);
            System.out.println(message.getSid());
            return "good";
        }
        catch (TwilioRestException e) {
            return e.toString();
        }
    }
	
	public boolean verifyTrader(int id, int code){
		Trader trader = traderRepository.getOne(id);
		if(trader != null){
			if(trader.getCode() == code){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean verifyAccount(int id, int code){
		Trader trader = traderRepository.getOne(id);
		if(trader != null && trader.getStatus() == Constant.NOT_VERIFY){
			if(trader.getCode() == code){
				Account account = accountrepository.getOne(trader.getPhoneNum());
				
				if(account != null){
					traderRepository.updateStatus(1, id);
					
					account.setEnabled(1);
					accountrepository.save(account);
					return true;
				}
				
				return false;
			}
		}
		
		return false;
	}
	
	public void resendVerify(int id){
		Trader Trader = traderRepository.getOne(id);
		if(Trader != null){
			if(Trader.getStatus() == Constant.NOT_VERIFY){
				Trader.setCode(twilloService.createVerifyCode());
				traderRepository.save(Trader);
				sendVerifyCode(Trader.getCode(), Trader.getPhoneNum());
			}
		}
	}
	
	public Trader getTraderNotActive(String phoneNum) {
		return traderRepository.getTraderNotActive(phoneNum, Constant.NOT_VERIFY);
	}
	
	public boolean changePass(String phoneNum, String pass){
		Account account = accountrepository.getActiveAccount(phoneNum, Constant.ROLE_TRADER);
		if(account != null){
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			TempAccount temp = new TempAccount(phoneNum, encoder.encode(pass));
			temp.setCode(twilloService.createVerifyCode());
			
			TempAccount old = tempAccRepository.getTemp(phoneNum);
			if(old != null) // if trader change pass before. so exists a tempAccount. then replace it 
				temp.setId(old.getId());
			tempAccRepository.save(temp);
			
			sendVerifyCode(temp.getCode(), phoneNum);
			return true;
		}
		
		return false;
	}
	
	public boolean confirmChangePass(String phone, int code){
		Account account = accountrepository.getActiveAccount(phone, Constant.ROLE_TRADER);
		if(account != null){
			TempAccount tempAccount = tempAccRepository.getTemp(account.getUserName());
			
			if(tempAccount != null){
				if(code == tempAccount.getCode()){
					account.setPassword(tempAccount.getPassword());
					accountrepository.save(account);
					
					tempAccRepository.deleteById(tempAccount.getId());
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		
		return false;
	}
	
	public int checkPhoneNum(String phoneNum) {
		Account account = accountrepository.getAccountByPhone(phoneNum, Constant.ROLE_TRADER);
		
		if(account != null){
			if(account.getEnabled() == 1) // this account activated
				return 1;
			else // this account did not active
				return 0;
		}
		
		// this phone number do not have account
		return -1;
	}
	
	public int checkVerifyPhoneNum(String phoneNum) {
		Trader trader = traderRepository.getTraderByPhone(phoneNum);

		if(trader != null){
			return trader.getStatus();
		}
		
		// this phone number do not have account
		return -2;
	}

}
