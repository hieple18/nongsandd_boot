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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nongsandd.constant.Constant;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name", length = Constant.COLUMN_LENGTH_30_LIMIT)
	private String name;

	@Column(name = "age")
	private int age;

	@Column(name = "ratingSum")
	private float ratingSum;

	@Column(name = "phoneNum", length = Constant.COLUMN_LENGTH_20_LIMIT)
	private String phoneNum;

	@Column(name = "dateCreate")
	private Date dateCreate;

	@Column(name = "code")
	private int code;

	@Column(name = "status")
	private int status;

	@Column(name = "commune")
	private int commune;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "address", referencedColumnName = "id")
	private Address address;

	@OneToMany(mappedBy = "user")
	private Set<Sale> saleSet = new HashSet<>();

	@OneToMany(mappedBy = "user")
	private Set<UserCmt> userCmts = new HashSet<>();

	@OneToMany(mappedBy = "trader")
	private Set<TraderCmt> traderCmts = new HashSet<>();

	@OneToMany(mappedBy = "user")
	private Set<UserNotification> userNotifications = new HashSet<>();

	////////////////////////////

	public User() {
		this.ratingSum = 0;
		this.age = 0;
		this.code = 0;
		this.status = 0;
	}
}