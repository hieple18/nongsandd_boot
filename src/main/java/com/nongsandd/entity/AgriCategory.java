package com.nongsandd.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.nongsandd.constant.Constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
*@author HiepLe
*@version 1.0 Dec 17, 2017
*/

@Entity
@Table(name="Agri_Category")
@Getter
@Setter
@NoArgsConstructor
public class AgriCategory implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "name", length = Constant.COLUMN_LENGTH_20_LIMIT)
    private String name;
    
    @OneToMany(mappedBy = "agriCategory")
    private Set<Agriculture> agricultures = new HashSet<>();
    
    public AgriCategory(String name) {
		super();
		this.name = name;
	}
    
    public AgriCategory(int id) {
		super();
		this.id = id;
	}
}
