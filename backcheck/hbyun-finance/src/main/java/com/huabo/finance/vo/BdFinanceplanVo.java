package com.huabo.finance.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="财务采集方案请求参数")
public class BdFinanceplanVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "方案名称")
	private String fname;

	@Schema(name = "数据源名称")
    private String dbconfigname;
    
    @Schema(name = "财务版本信息名称")
    private String fversionname;
    
    @Schema(name = "配置公司名称")
    private String financeorgname;
    
    @Schema(name = "创建人姓名")
    private String creatorname;
    
    @Schema(name = "所属公司")
    private BigDecimal linkOrgId;
    
    @Schema(name = "采集方案状态  0-未启用 1-启用 2-弃用")
    private Integer fstatus;

}
