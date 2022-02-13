package com.ctc.demo.h2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "SYS_CFG_TBL")
public class SystemConfig {
	@Id
	@Column(nullable=false, length=255)
	private String key;

	@Column(nullable=false, length=255)
	private String value;
}
