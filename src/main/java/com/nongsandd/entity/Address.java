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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nongsandd.constant.Constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Address")
@Getter
@Setter
@NoArgsConstructor
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name="lat") 
    private double lat;
    
    @Column(name="lng") 
    private double lng;
    
    @Column(name = "address", length = Constant.COLUMN_LENGTH_50_LIMIT)
    private String address;
    
    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = "address")
    private User user;
    
    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = "address")
    private Trader trader;
    
    @OneToOne(fetch=FetchType.LAZY,
            mappedBy = "address")
    private Sale sale;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "hamletID")
    private Hamlet hamlet;
     
	public Address(double lat, double lng, Hamlet hamlet) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.hamlet = hamlet;
	}

	public Address(double lat, double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}
}
