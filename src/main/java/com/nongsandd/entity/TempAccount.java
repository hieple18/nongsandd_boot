package com.nongsandd.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nongsandd.constant.Constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: HiepLe
 * @version: Jun 10, 2018
 */

@Entity
@Table(name="Temp_account")
@Getter
@Setter
@NoArgsConstructor
public class TempAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
    
    @Column(name="username", length = Constant.COLUMN_LENGTH_20_LIMIT)
    private String userName;
    
    @Column(name="password", length = Constant.COLUMN_LENGTH_100_LIMIT)
    private String password;
    
    @Column(name="code")    
    private int code;

	public TempAccount(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}
}