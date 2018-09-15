package com.nongsandd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nongsandd.constant.Constant;
import com.nongsandd.entity.Account;
import com.nongsandd.entity.Sale;
import com.nongsandd.repository.AccountRepository;
import com.nongsandd.repository.SaleRepository;

/**
 * @author: HiepLe
 * @version: May 9, 2018
 */

@Service
public class AdminService {
	@Autowired
	private AccountRepository accountReponsitory;

	@Autowired
	private AddressService addressService;

	@Autowired
	private SaleRepository saleRepository;
	
	public void updateCommuneID(){
		List<Object[]> objects = saleRepository.getSaleCommune();
		for (Object[] object : objects) {
			Sale sale = (Sale)object[0];
			int hamletID = (int)object[1];
			int communeID = addressService.getCommuneIDByHamletID(hamletID);
			sale.setCommuneID(communeID);
			
			saleRepository.save(sale);
		}
	}
	
	public void createAdmin(){
		String userName = "hiep2218";
		String pass = "ngunhucutVan";
		
		Account account = accountReponsitory.getAccountByPhone(userName, Constant.ROLE_ADMIN);
		if(account == null){
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			account = new Account(userName, encoder.encode(pass), Constant.ROLE_ADMIN, Constant.ENABLE_STATE);
			accountReponsitory.save(account);
		}
	}

}