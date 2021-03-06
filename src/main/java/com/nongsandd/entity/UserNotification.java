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
@Table(name="user_Notification")
@Getter
@Setter
@NoArgsConstructor
public class UserNotification implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "userID", referencedColumnName = "id")
    private User user;
    
    @Column(name = "content", length = Constant.COLUMN_LENGTH_100_LIMIT)
    private String content;
    
    @Column(name = "link", length = Constant.COLUMN_LENGTH_100_LIMIT)
    private String link;
    
    @Column(name = "dateCreate")
    private long dateCreate;
    
    @Column(name = "timeAgo", length = Constant.COLUMN_LENGTH_10_LIMIT)
    private String timeAgo;
    
    @Column(name = "status")
    private int status;

	public UserNotification(User user, String content, String link, long dateCreate, int status) {
		super();
		this.user = user;
		this.content = content;
		this.link = link;
		this.dateCreate = dateCreate;
		this.status = status;
	}
    
}