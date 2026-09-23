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
public class EvaluationTrackingStatisticsResult {

	@Schema(name="审批状态")
	private Integer status;

	@Schema(name="统计")
	private Integer num;
	
	@Schema(name="组织ids")
	@TableField(exist=false)
	private  List<BigDecimal> orgIds;
	
	@Schema(name="组织ids")
	@TableField(exist=false)
	private BigDecimal orgid;
	
}
