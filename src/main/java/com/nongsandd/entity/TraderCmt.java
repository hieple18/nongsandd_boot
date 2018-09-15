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

import com.nongsandd.constant.Constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="trader_Cmt")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraderCmt implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "traderID", referencedColumnName = "id")
    private Trader trader;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "userID", referencedColumnName = "id")
    private User user;
    
    @Column(name = "content", length = Constant.COLUMN_LENGTH_300_LIMIT)
    private String content;
    
    @Column(name = "ratingCount")
    private int ratingCount;
    
    @Column(name = "dateCreate")
    private Date dateCreate;
    
    @Column(name = "status")
    private int status;

	public TraderCmt(Trader trader, User user, String content, int ratingCount, Date dateCreate, int status) {
		super();
		this.trader = trader;
		this.user = user;
		this.content = content;
		this.ratingCount = ratingCount;
		this.dateCreate = dateCreate;
		this.status = status;
	}
    
}
