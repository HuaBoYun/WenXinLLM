package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.entity.TblNbsjPlanProject;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="审计计划查询对象")
public class TblNbsjAuditPlanVo {
		
	@Schema(name = "计划编码")
	private String plancode;

	@Schema(name = "计划名称")
	private String planname;

	@Schema(name = "计划年度")
	private String palnyear;
	
	@Schema(name = "计划负责人 用户表外键")
    private BigDecimal principalid;
}
