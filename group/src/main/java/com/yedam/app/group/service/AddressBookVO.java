package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class AddressBookVO {

	private Integer addressBookId;
	private String addressBookName;
	private String addressBookNumber;
	private String addressBookEmail;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date addressBookUpdateDate;
	private String addressBookCompanyName;
	private String addressBookCompanyNumber;
	private String employeeId;
	private String zipCode;
	private String streetAddress;
	private String addressBookType;
	private String bookRecode;
	private String bookDepartmentType;
	
    private Integer employeeNo;
}