package com.huabo.monitor.vo.result;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestElementResult {
	 
	private String TYPENAME;
	private String ELEMENTCODE;
	private String BUSINESSDESC;
	private String RISKTYPE;
	private String CONTROLTARGET;
	private String CONTROLMEASURES;
	private String CHECKMETHOD;
	private String PROCEDURES;
	private String LONGSTRING1;
	private String LONGSTRING2;
	private String TESTRESULT;
	private String EXECUTEPOINTVALIDITY;
	private String DESIGNPOINTVALIDITY;
	private String TESTPOINTVALIDITY;

}
