package com.nongsandd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: HiepLe
 * @version: May 22, 2018
 */

@Entity
@Table(name="Price_Record")
@Getter
@Setter
@NoArgsConstructor
public class PriceRecord {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
	
	@Column(name = "year")
    private int year;
	
	@Column(name = "month")
    private int month;
	
	@Column(name = "max")
    private float max;
	
	@Column(name = "min")
    private float min;	
}
