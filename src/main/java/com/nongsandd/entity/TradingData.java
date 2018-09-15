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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Trading_Data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradingData implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "traderID", referencedColumnName = "id")
    private Trader trader;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "saleID", referencedColumnName = "id")
    private Sale sale;
    
    @Column(name = "dateRequest")
    private Date dateRequest;
    
    @Column(name = "dateSelected")
    private Date dateSelected;
    
    @Column(name = "status")
    private int status;

	public TradingData(Trader trader, Sale sale, Date dateRequest, Date dateSelected, int status) {
		super();
		this.trader = trader;
		this.sale = sale;
		this.dateRequest = dateRequest;
		this.dateSelected = dateSelected;
		this.status = status;
	}
}