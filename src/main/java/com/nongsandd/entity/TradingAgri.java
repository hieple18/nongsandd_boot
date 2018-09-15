package com.nongsandd.entity;

import java.io.Serializable;

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

@Entity
@Table(name="Trading_Agri")
@Getter
@Setter
@NoArgsConstructor
public class TradingAgri implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "traderID", referencedColumnName = "id")
    private Trader trader;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "agriID", referencedColumnName = "id")
    private Agriculture agriculture;

	public TradingAgri(Trader trader, Agriculture agriculture) {
		super();
		this.trader = trader;
		this.agriculture = agriculture;
	}

}

