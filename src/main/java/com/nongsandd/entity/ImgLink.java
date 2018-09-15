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

import com.nongsandd.constant.Constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Img_Link")
@Getter
@Setter
@NoArgsConstructor
public class ImgLink implements Serializable {
	private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    
    @Column(name="name", length = Constant.COLUMN_LENGTH_20_LIMIT)
    private String name;
    
    @Column(name="link", length = Constant.COLUMN_LENGTH_200_LIMIT)
    private String link;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "saleID", referencedColumnName = "id")
    private Sale sale;

	public ImgLink(String name, String link, Sale sale) {
		super();
		this.name = name;
		this.link = link;
		this.sale = sale;
	}
}
