package com.huabo.finance.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 辅助账余额
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="TblConfigTableInfo", description="业务数据表配置信息")
public class TblConfigTableInfoVo extends BaseVo implements Serializable {

	 private static final long serialVersionUID = 1L;

     @Schema(name = "表注释")
   private String fname;

     @Schema(name = "所属方案名称")
   private String planName;
     
     @Schema(name = "所属方案主键")
     private String planid;
     
     
     private BigDecimal orgId;

}
