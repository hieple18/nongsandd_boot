package com.nongsandd.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: HiepLe
 * @version: May 15, 2018
 */

@Entity
@Table(name="Mining")
@Getter
@Setter
public class Mining implements Serializable{
	
	private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    
 // using for recommend with distinguish agriculture id
    /*@Column(name="agriID")
    private int agriID;*/
    
    @Column(name="area")
    private String area;
    
    @Column(name="agri")
    private String agri;
    
    @Column(name="commmuneID")
    private String communeID;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "traderID", referencedColumnName = "id")
    private Trader trader;
    
    public Mining(){
    	area = "";
    	agri = "";
    	communeID = "";
    };
	
    public void setArea(String area) {
		this.area = area;
	}

	public HashMap<Integer, Integer> getArea() {
		if(area.equals("")){
			return new HashMap<>(); 
		}else{
			return convertToHashMap(area);
		}
	}

	public HashMap<Integer, Integer> getAgri() {
		if(area.equals("")){
			return new HashMap<>(); 
		}else{
			return convertToHashMap(agri);
		}
		
	}

	public void setAgri(String agri) {
		this.agri = agri;
	}

	public HashMap<Integer, Integer> getCommuneID() {
		if(area.equals("")){
			return new HashMap<>(); 
		}else{
			return convertToHashMap(communeID);
		}
	}

	public void setCommuneID(String communeID) {
		this.communeID = communeID;
	}
    
    public HashMap<Integer, Integer> convertToHashMap(String s){
    	HashMap<Integer, Integer> maps = new HashMap<>();
		
		String[] parts1 = s.split(";");
		for (String string : parts1) {
			String[] parts2 = string.split(",");
			int key = Integer.parseInt(parts2[0]);
			int value = Integer.parseInt(parts2[1]);
			maps.put(key, value);
		}
		
		return maps;
    }
    
    public void update(HashMap<Integer, Integer> areas, HashMap<Integer, Integer> quanlitys, 
    		HashMap<Integer, Integer> communeIDs, Trader trader){
    	// init string
    	area = "";
    	agri = "";
    	communeID = "";
    	
    	// convent hashmap to string
    	for (Map.Entry<Integer, Integer> entry : areas.entrySet()) {
			area = area + entry.getKey().toString() + "," + entry.getValue().toString() + ";"; 
		}

    	for (Map.Entry<Integer, Integer> entry : quanlitys.entrySet()) {
    		agri = agri + entry.getKey().toString() + "," + entry.getValue().toString() + ";"; 
		}

    	for (Map.Entry<Integer, Integer> entry : communeIDs.entrySet()) {
    		communeID = communeID + entry.getKey().toString() + "," + entry.getValue().toString() + ";"; 
		}
    	
    	// remove ; character in the end
    	area = area.substring(0, area.length() - 1);
    	agri = agri.substring(0, agri.length() - 1);
    	communeID = communeID.substring(0, communeID.length() - 1);
    	
    	this.trader = trader;
    }
    
    public boolean isEmpty(){
    	if(!"".equals(this.agri))
    		return false;
    	if(!"".equals(this.communeID))
    		return false;
    	if(!"".equals(this.area))
    		return false;
    	
    	return true;
    }
}
