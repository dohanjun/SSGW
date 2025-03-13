package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class GroupVO {
	    private Long empId;
	    private String empName;
	    private String gender;
	    private Integer age;
	    private Date hireDate;
	    private String etc;

}
