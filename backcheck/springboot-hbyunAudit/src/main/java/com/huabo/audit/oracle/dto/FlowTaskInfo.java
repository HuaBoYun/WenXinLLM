package com.huabo.audit.oracle.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class FlowTaskInfo {
	
	private BigDecimal currentStaffId;
	
	private String currentStaffName;
	
	private Date createTime;
	
	private String commont;
	
	private String fromId;
	
	private String createTimeStr;

}
