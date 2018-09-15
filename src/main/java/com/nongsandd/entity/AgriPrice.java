package com.nongsandd.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
*@author HiepLe
*@version 1.0 Dec 23, 2017
*/

@Entity
@Table(name="Agri_Price")
@Getter
@Setter
@NoArgsConstructor
public class AgriPrice implements Serializable{
	private static final long serialVersionUID = 1L;  
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "price")
    private float price;
    
    @Column(name = "pricechange")
    private float priceChange;
    
    @Column(name = "date")
    private Date date;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "agriID", referencedColumnName = "id")
    private Agriculture agriculture;

	public AgriPrice(float price, float priceChange, Date date, Agriculture agriculture) {
		super();
		this.price = price;
		this.priceChange = priceChange;
		this.date = date;
		this.agriculture = agriculture;
	}
}
