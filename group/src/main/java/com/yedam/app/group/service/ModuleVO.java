package com.yedam.app.group.service;

import lombok.Data;

@Data
public class ModuleVO {
    private int moduleNo;       // 모듈 번호
    private String moduleName;  // 모듈 이름
    private double modulePrice; // 1달 모듈 가격
    private String version;     // 버전
    private char basicModule;   // 기본 모듈 여부 (Y/N)
    private char activate;      // 활성화 여부 (Y/N)
    private String explanation; // 모듈 설명
}
