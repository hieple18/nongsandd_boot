package com.nongsandd.entity;
/**
*@author HiepLe
*@version 1.0 Nov 14, 2017
*/

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nongsandd.constant.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="username", length = Constant.COLUMN_LENGTH_20_LIMIT)
    private String userName;
    
    @Column(name="password", length = Constant.COLUMN_LENGTH_100_LIMIT)
    private String password;
    
    @Column(name="role", length = Constant.COLUMN_LENGTH_20_LIMIT)    
    private String role;
    
    @Column(name="enabled") 
    private int enabled;
}
