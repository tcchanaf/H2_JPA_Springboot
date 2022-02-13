package com.ctc.demo.h2.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Data
@Table(name = "USER_INFO")
public class UserInfo {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(nullable=false, length=255)
	private String userName;
	@Column
	private String gender;
	@Column
	private Short age;
	@Column
	private String lastLoginIpAddress;
	@Column
	private Date createDatetime;

}
