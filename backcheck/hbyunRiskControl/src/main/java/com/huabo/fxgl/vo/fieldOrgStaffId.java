package com.huabo.fxgl.vo;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 复制ID
 * */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class fieldOrgStaffId {
	@Schema(name="预留人员单选1")
	private BigDecimal staffid1;
	
	@Schema(name="预留人员单选2")
	private BigDecimal staffid2;
	
	@Schema(name="预留人员单选3")
	private BigDecimal staffid3;
	
	@Schema(name="预留人员单选4")
	private BigDecimal staffid4;
	
	@Schema(name="预留人员单选5")
	private BigDecimal staffid5;

	@Schema(name="预留人员多选1")
	private String staffids1;
	
	@Schema(name="预留人员多选2")
	private String staffids2;
	
	@Schema(name="预留人员多选3")
	private String staffids3;
	
	@Schema(name="预留人员多选4")
	private String staffids4;
	
	@Schema(name="预留人员多选5")
	private String staffids5;
	
	@Schema(name="预留组织单选1")
	private BigDecimal orgid1;
	
	@Schema(name="预留组织单选2")
	private BigDecimal orgid2;

	@Schema(name="预留组织单选3")
	private BigDecimal orgid3;
	
	@Schema(name="预留组织单选4")
	private BigDecimal orgid4;
	
	@Schema(name="预留组织单选5")
	private BigDecimal orgid5;
	
	@Schema(name="预留组织多选1")
	private String orgids1;
	
	@Schema(name="预留组织多选2")
	private String orgids2;
	
	@Schema(name="预留组织多选3")
	private String orgids3;
	
	@Schema(name="预留组织多选4")
	private String orgids4;

	@Schema(name="预留组织多选5")
	private String orgids5;
	
}
