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
public class TestTrackingStatisticsResult {

	@Schema(name="计划状态")
	private String planstatus;

	@Schema(name="统计")
	private Integer num;
	
	
	@Schema(name="组织ids")
	@TableField(exist=false)
	private  List<BigDecimal> orgIds;
	
	@Schema(name="组织ids")
	@TableField(exist=false)
	private BigDecimal orgid;
	
	
	
}
