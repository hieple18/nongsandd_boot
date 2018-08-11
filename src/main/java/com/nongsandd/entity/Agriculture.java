package com.nongsandd.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.nongsandd.entity.AgriPrice;
import com.nongsandd.constant.Constant;

/**
*@author HiepLe
*@version 1.0 Dec 17, 2017
*/

@Entity
@Table(name="agriculture")
public class Agriculture implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "name", length = Constant.COLUMN_LENGTH_30_LIMIT)
    private String name;
    
    @Column(name = "unit", length = Constant.COLUMN_LENGTH_10_LIMIT)
    private String unit;
    
    @Column(name = "status")
    private int status;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    private AgriCategory agriCategory;
    
    @OneToMany(mappedBy = "agriculture")
    private Set<AgriPrice> priceSet = new HashSet<>();

	//////

	public Agriculture(){}
	
	public Agriculture(int id){
		this.id = id;
	}
	
	public Agriculture(String name, String unit, int status, AgriCategory agriCategory) {
		super();
		this.name = name;
		this.unit = unit;
		this.status = status;
		this.agriCategory = agriCategory;
	}

	public AgriCategory getAgriCategory() {
		return agriCategory;
	}

	public void setAgriCategory(AgriCategory agriCategory) {
		this.agriCategory = agriCategory;
	}

	public Set<AgriPrice> getPriceSet() {
		return priceSet;
	}

	public void setPriceSet(Set<AgriPrice> priceSet) {
		this.priceSet = priceSet;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
    
}
