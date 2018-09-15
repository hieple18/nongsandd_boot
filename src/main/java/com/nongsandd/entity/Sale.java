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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nongsandd.constant.Constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Sale")
@Getter
@Setter
@NoArgsConstructor
public class Sale implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "quantity", nullable = true)
    private float quantity;
    
    @Column(name = "area")
    private float area;
    
    @Column(name = "sellingDescribe", length = Constant.COLUMN_LENGTH_300_LIMIT, nullable = true)
    private String describe;
    
    @Column(name = "dateCreate")
    private Date dateCreate;
    
    @Column(name = "price", nullable = true)
    private long price;
    
    @Column(name = "requestCount")
    private int requestCount;
    
    @Column(name = "communeID")
    private int communeID;
    
    @Column(name = "status")
    private int status;

	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "address", referencedColumnName = "id")
    private Address address;
    
    @OneToMany(mappedBy = "sale")
    private Set<TradingData> datas = new HashSet<>();
    
    @OneToMany(mappedBy = "sale")
    private Set<TradingHistory> histories = new HashSet<>();
    
    @OneToMany(mappedBy = "sale")
    private Set<ImgLink> imgLinks = new HashSet<>();
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "userID", referencedColumnName = "id")
    private User user;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "agriID", referencedColumnName = "id")
    private Agriculture agriculture;

	public Sale(int id) {
		super();
		this.id = id;
	}
}