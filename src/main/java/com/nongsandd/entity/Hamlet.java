package com.nongsandd.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="hamlet")
@Getter
@Setter
public class Hamlet implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "hamletID")
    private int hamletID;
    
    @Column(name = "name", length = Constant.COLUMN_LENGTH_30_LIMIT)
    private String name;
    
    @Column(name = "address", length = Constant.COLUMN_LENGTH_50_LIMIT)
    private String address;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "communeID")
    private Commune commune;
    
    @OneToMany(mappedBy = "hamlet")
    private Set<Address> adressSet = new HashSet<>();

}
