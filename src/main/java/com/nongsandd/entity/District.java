package com.nongsandd.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.nongsandd.constant.Constant;

import lombok.Getter;
import lombok.Setter;

/**
*@author HiepLe
*@version 1.0 Dec 9, 2017
*/

@Entity
@Table(name="district")
@Getter
@Setter
public class District implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "districtID")
    private int districtID;
    
    @Column(name = "name", length = Constant.COLUMN_LENGTH_30_LIMIT)
    private String name;
    
    @Column(name = "provinceID")
    private String provinceID;
    
    @OneToMany(mappedBy = "district")
    private Set<Commune> communeSet = new HashSet<>();

}
