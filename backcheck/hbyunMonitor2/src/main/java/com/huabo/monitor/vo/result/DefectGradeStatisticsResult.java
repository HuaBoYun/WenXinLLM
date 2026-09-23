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
public class DefectGradeStatisticsResult {

	@Schema(name="缺陷等级")
	private String defecttype;

	@Schema(name="统计")
	private Integer num;
	
	
	@Schema(name="组织ids")
	@TableField(exist=false)
	private  List<BigDecimal> orgIds;
	
	@Schema(name="组织ids")
	@TableField(exist=false)
	private BigDecimal orgid;
}
