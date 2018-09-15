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
import lombok.Setter;

@Entity
@Table(name="Trading_History")
@Getter
@Setter
public class TradingHistory implements Serializable {

private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "traderID", referencedColumnName = "id")
    private Trader trader;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "saleID", referencedColumnName = "id")
    private Sale sale;
    
    @Column(name = "content")
    private String content;
    
    @Column(name = "status")
    private int status;
}
