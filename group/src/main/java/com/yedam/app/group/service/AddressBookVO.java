package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class AddressBookVO {

	private int addressBookId;
	private String addressBookName;
	private String addressBookNumber;
	private String addressBookEmail;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date addressBookUpdateDate;
	private String addressBookCompanyName;
	private String addressBookCompanyNumber;
	private Integer employeeId;
	private String zipCode;
	private String streetAddress;
	private String addressBookType;
}
