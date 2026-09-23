package com.huabo.audit.oracle.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import java.math.BigDecimal;

import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="我的任务清单列表查询入参")
public class TblNbsjOperateVo extends BaseVo{
	
	@Schema(name = "业务单元")
    private String businessType;
	@Schema(name="当前用户id",hidden=true)
	private BigDecimal staffid;
	@Schema(name="项目id",hidden=true)
	private BigDecimal projectId;
	@Schema(name = "节点id")
	private BigDecimal targetId;
	
	@Schema(name = "审计程序")
	private String suditProcess;
	
	@Schema(name = "风险归属")
	private String riskAttrbution;
	
	@Schema(name="是否查看",hidden=true)
	private Integer status;

}
