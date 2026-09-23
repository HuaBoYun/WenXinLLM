package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;

import javax.persistence.Column;

import com.baomidou.mybatisplus.annotation.TableField;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="内控问题汇总列表查询入参")
public class TblTesttaskProblemFindVo extends BaseVo{
 
	@Schema(name = "一级流程")
	private String oneprocess;

	@Schema(name = "内控评价年度")
	private int testYear;//

	@Schema(name = "问题类别")
	private String problemtype;

	@Schema(name = "缺陷等级")
	private String defectlevel;

	@Schema(name = "创建人")
	private BigDecimal createstaffid;//
}
