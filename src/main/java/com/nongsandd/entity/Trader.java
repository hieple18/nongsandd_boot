package com.nongsandd.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nongsandd.constant.Constant;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Trader")
@Getter
@Setter
public class Trader implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    
    @Column(name="name", length = Constant.COLUMN_LENGTH_30_LIMIT)
    private String name;
    
    @Column(name="age")    
    private int age;
    
    @Column(name="ratingSum")    
    private float ratingSum;
    
    @Column(name="phoneNum", length = Constant.COLUMN_LENGTH_20_LIMIT)    
    private String phoneNum;
    
    @Column(name="dateCreate") 
    private Date dateCreate;
    
    @Column(name="code") 
    private int code;
    
    @Column(name="commune") 
    private int commune;
    
    @Column(name="status")
    private int status;

	@OneToOne()
    @JoinColumn(name = "account", referencedColumnName = "username")
    private Account account;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "address", referencedColumnName = "id")
    private Address address;

    @OneToOne(mappedBy = "trader")
    private Mining mining;
    
    @OneToMany(mappedBy = "trader")
    private Set<TradingAgri> tradingSet = new HashSet<>();
    
    @OneToMany(mappedBy = "trader")
    private Set<TradingData> requestSet = new HashSet<>();
    
    @OneToMany(mappedBy = "trader")
    private Set<UserCmt> userCmts = new HashSet<>();
    
    @OneToMany(mappedBy = "trader")
    private Set<TraderCmt> traderCmts = new HashSet<>();
    
    @OneToMany(mappedBy = "trader")
    private Set<TraderNotification> traderNotifications = new HashSet<>();

    public Trader(){
    	this.ratingSum = 0;
    }

	/**
	 * @param traderID
	 */
	public Trader(int traderID) {
		// TODO Auto-generated constructor stub
		this.id = traderID;
	}
    	
}