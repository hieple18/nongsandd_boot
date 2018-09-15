package com.nongsandd.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nongsandd.constant.Constant;
import com.nongsandd.entity.Mining;
import com.nongsandd.entity.Sale;
import com.nongsandd.entity.Trader;
import com.nongsandd.model.SaleScore;
import com.nongsandd.repository.MiningRepository;
import com.nongsandd.repository.SaleRepository;
import com.nongsandd.repository.TradingDataRepository;

/**
 * @author: HiepLe
 * @version: Sep 6, 2018
 */

@Service
public class MiningService {
	@Autowired
	private MiningRepository miningRepository;
	
	@Autowired
	private SaleRepository saleRepository;

	@Autowired
    private TradingDataRepository dataRepository;
	
	@Autowired
	private SaleService saleService;
	
	public int getMaxForMining(HashMap<Integer, Integer> map){
		int max = 0;
		for (Entry<Integer, Integer> index : map.entrySet()) {
			if(index.getValue() > max){
				max = index.getValue();
			}
		}
		
		return max;
	}
	
	// calculate X value for recommend
	public HashMap<Integer, Float> caculateXForMining(HashMap<Integer, Integer> areas, HashMap<Integer, Integer> ids, 
    		HashMap<Integer, Integer> communeIDs, int sum_new){
		int maxArea = Collections.max(areas.values());
		int maxID = Collections.max(ids.values());
		int maxCommune = Collections.max(communeIDs.values());

		
		float sum = maxArea + maxCommune + maxID;
		float M_id = (float)maxID/sum;
		float M_area = (float)maxArea/sum;
		float M_comm = (float)maxCommune/sum;
		
		HashMap<Integer, Float> result = new HashMap<>();
		result.put(Constant.AGRI_M, M_id);
		result.put(Constant.AREA_M, M_area);
		result.put(Constant.COMMUNE_M, M_comm);
		
		return result;
	}
	
	public List<SaleScore> recommendSale(int traderID){
		List<Sale> sales = saleService.getSaleListByTrader(traderID);
		ArrayList<SaleScore> scores = new ArrayList<>();
		
		// if trader do not have any info to recommend then return list order by date create
		Mining mining = miningRepository.getMiningByTrader(traderID);
		if(mining == null || mining.isEmpty()) {
			for (Sale sale : sales) {
				scores.add(new SaleScore(sale, 0));
			}
			
			return scores;
		}
		
		// if trader have info to run mining
		HashMap<Integer, Integer> areas = mining.getArea();
		HashMap<Integer, Integer> agris = mining.getAgri();
		HashMap<Integer, Integer> communeIDs = mining.getCommuneID();
		
		//get X value
		int sum = dataRepository.getCountSale(traderID);
		HashMap<Integer, Float> X = caculateXForMining(areas, agris, communeIDs, sum);
		float X_area = X.get(Constant.AREA_M);
		float X_id = X.get(Constant.AGRI_M);
		float X_comm = X.get(Constant.COMMUNE_M);
		
		int area, communeID, id;
		int areaScore, idScore, commScore;

		for(int i = 0; i < sales.size(); i++){
			Sale sale = sales.get(i);
			area = (int)sale.getArea();
			id = sale.getAgriculture().getId();
			communeID = sale.getCommuneID();	
			
			areaScore = areas.get(area) == null ? 0 : areas.get(area);
			idScore = agris.get(id) == null ? 0 : agris.get(id);
			commScore = communeIDs.get(communeID) == null ? 0 : communeIDs.get(communeID);
			int score = (int) (X_area*areaScore + X_comm*commScore + X_id*idScore);
			
			scores.add(new SaleScore(sale, score));
		}
		
		Collections.sort(scores);
		Collections.reverse(scores);

		return scores;
	}
	
	public void updateMining(int traderID, int saleID){
		Sale sale = saleRepository.getOne(saleID);
		Trader trader = new Trader(traderID);
		
		Mining mining = miningRepository.getMiningByTrader(traderID);
		
		HashMap<Integer, Integer> areas = mining.getArea();
		HashMap<Integer, Integer> agris = mining.getAgri();
		HashMap<Integer, Integer> communeIDs = mining.getCommuneID();

		
		int area, id, communeID;
		int areaScore, quanScore, commScore;
		
		area = (int)sale.getArea();
		id = sale.getAgriculture().getId();
		communeID = sale.getCommuneID();
		
		// recount score
		areaScore = areas.get(area) == null ? 1 : areas.get(area) + 1;
		quanScore = agris.get(id) == null ? 1 : agris.get(id) + 1;
		commScore = communeIDs.get(communeID) == null ? 1 : communeIDs.get(communeID) + 1;
		
		// update new score to hashmap
		areas.put(area, areaScore);
		agris.put(id, quanScore);
		communeIDs.put(communeID, commScore);
		
		// save mining
		mining.update(areas, agris, communeIDs, trader);
		miningRepository.save(mining);
	}
	
	public void updateAllMining(int traderID){
		List<Sale> sales = dataRepository.getSaleForMining(traderID);
		Trader trader = new Trader(traderID);
		
		Mining mining = miningRepository.getMiningByTrader(traderID);
		
		HashMap<Integer, Integer> areas = mining.getArea();
		HashMap<Integer, Integer> agris = mining.getAgri();
		HashMap<Integer, Integer> communeIDs = mining.getCommuneID();

		int area, id, communeID;
		int areaScore, quanScore, commScore;
		for(int i = 0; i < sales.size(); i++){
			Sale sale = sales.get(i);
			area = (int)sale.getArea();
			id = sale.getAgriculture().getId();
			communeID = sale.getCommuneID();
			
			// recount score
			areaScore = areas.get(area) == null ? 1 : areas.get(area) + 1;
			quanScore = agris.get(id) == null ? 1 : agris.get(id) + 1;
			commScore = communeIDs.get(communeID) == null ? 1 : communeIDs.get(communeID) + 1;
			
			// update new score to hashmap
			areas.put(area, areaScore);
			agris.put(id, quanScore);
			communeIDs.put(communeID, commScore);
		}
		
		// save mining
		mining.update(areas, agris, communeIDs, trader);
		miningRepository.save(mining);
	}
	
	// this function using for recommend sale for trader with distinguish agriculture id
	/*public List<SaleScore> miningWithAgriID(int traderID){
		List<Sale> sales = getSaleListByTrader(traderID);
		int agriID = sales.get(0).getAgriculture().getId();
		ArrayList<SaleScore> scores = new ArrayList<>();
		
		// if trader do not have any info to recommend then return list order by date create
		Mining mining = miningRepository.getMiningByTrader(traderID, agriID);
		if(mining == null || mining.isEmpty()) {
			for (Sale sale : sales) {
				scores.add(new SaleScore(sale, 0));
			}
			
			return scores;
		}
		
		// if trader have info to run mining
		HashMap<Integer, Integer> areas = mining.getArea();
		HashMap<Integer, Integer> agris = mining.getAgri();
		HashMap<Integer, Integer> communeIDs = mining.getCommuneID();

		// list sale sort by id. this var to detect when id change
		int currID = agriID;
		
		int area, communeID;
		int areaScore, idScore, commScore;

		for(int i = 0; i < sales.size(); i++){
			Sale sale = sales.get(i);
			int id = sale.getAgriculture().getId();
			if(id == currID){
				area = (int)sale.getArea();
				id = sale.getAgriculture().getId();
				communeID = sale.getCommuneID();	
				
				areaScore = areas.get(area) == null ? 0 : areas.get(area);
				idScore = agris.get(id) == null ? 0 : agris.get(id);
				commScore = communeIDs.get(communeID) == null ? 0 : communeIDs.get(communeID);
				int score = areaScore + commScore + idScore;
				
				scores.add(new SaleScore(sale, score));
			}else{
				currID = sale.getAgriculture().getId();
				
				mining = miningRepository.getMiningByTrader(traderID, currID);
				if(mining == null) {
					mining = new Mining();
				}
				areas = mining.getArea();
				agris = mining.getAgri();
				communeIDs = mining.getCommuneID();
				
				i--;
			}
		}
		
		Collections.sort(scores);
		Collections.reverse(scores);

		return scores;
	}
	
	// this function using for recommend sale for trader with distinguish agriculture id
	public void updateWithAgriID(int traderID){
		List<Sale> sales = dataRepository.getSaleForMining(traderID);
		Trader trader = new Trader(traderID);
		
		HashMap<Integer, Integer> areas = new HashMap<>();
		HashMap<Integer, Integer> agris = new HashMap<>();
		HashMap<Integer, Integer> communeIDs = new HashMap<>();
		
		// list sale sort by id. this var to detect when id change
		int currID = sales.get(0).getAgriculture().getId();
		
		int area, id, communeID, agriID;
		int areaScore, quanScore, commScore;
		for(int i = 0; i < sales.size(); i++){
			Sale sale = sales.get(i);
			agriID = sale.getAgriculture().getId();
			
			if(agriID == currID){ // id not change
				area = (int)sale.getArea();
				id = sale.getAgriculture().getId();
				communeID = sale.getCommuneID();
				
				// recount score
				areaScore = areas.get(area) == null ? 1 : areas.get(area) + 1;
				quanScore = agris.get(id) == null ? 1 : agris.get(id) + 1;
				commScore = communeIDs.get(communeID) == null ? 1 : communeIDs.get(communeID) + 1;
				
				// update new score to hashmap
				areas.put(area, areaScore);
				agris.put(id, quanScore);
				communeIDs.put(communeID, commScore);
			}else{ // id change
				
				// save mining
				Mining mining = new Mining();
				mining.update(areas, agris, communeIDs, currID, trader);
				miningRepository.save(mining);
				
				// init hashmap to store next id in list
				areas = new HashMap<>();
				agris = new HashMap<>();
				communeIDs = new HashMap<>();
				
				currID = sale.getAgriculture().getId();
				i--;
			}
		}
	}*/
}
