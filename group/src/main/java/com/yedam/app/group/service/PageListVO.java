package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PageListVO extends SearchVO{

	private Integer mailId;
	private String title;
	private String content;
	private int sentState;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date sentDate;
	private String employeeId;
	private String attachedFileName;
	private String getUser;
	private String cc;
	private String recode;
	private String mailType;

	private String sortKey;
	
	private Integer addressBookId;
	private String addressBookName;
	private String addressBookNumber;
	private String addressBookEmail;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date addressBookUpdateDate;
	private String addressBookCompanyName;
	private String addressBookCompanyNumber;
	private String zipCode;
	private String streetAddress;
	private String addressBookType;
}
