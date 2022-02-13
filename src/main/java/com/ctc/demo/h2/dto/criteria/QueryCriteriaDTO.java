package com.ctc.demo.h2.dto.criteria;

import java.util.Date;

import lombok.Data;

@Data
public class QueryCriteriaDTO {

	private String userName;
	private Date createDatetimeStart;
	protected Date createDatetimeEnd;
}
