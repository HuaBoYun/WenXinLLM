package com.huabo.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.finance.vo.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 财务数据采集记录表
 * </p>
 *
 * @author L
 * @since 2025-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("BD_IMPORTBATCHS_RECORD")
@Schema(name="BdImportBatchsRecord对象", description="业务数据导入记录表")
public class BdImportBatchsRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    @TableId("IMPORTID")
    private String importId;

	  @Schema(name = "导入名称")
	  @TableField("RECORDNAME")
	private String recordname;
	
	  @Schema(name = "所属业务表主键")
	  @TableField("TABLEID")
	private String tableId;
	
	  @Schema(name = "开始时间")
	  @TableField("STARTDATE")
	  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startdate;
	
	  @Schema(name = "结束时间")
	  @TableField("ENDDATE")
	  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date enddate;
	
	  @Schema(name = "是否完成 0-未开始 1-采集中，2-已完成")
	  @TableField("ISCOMPLETED")
	private Integer iscompleted;
	  
	  @Schema(name = "采集结果，0-失败 ， 1-成功 ")
	  @TableField("ISRESULT")
	private Integer isresult;
	  
	  @Schema(name = "导入批次顺序 ")
	  @TableField("BATCHNUM")
	private Integer batchNum; 
	
	  @Schema(name = "创建时间")
	  @TableField("CREATETIME")
	  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createtime;
	  
	  @Schema(name = "访问地址")
	  @TableField("RECORDIP")
	private String recordip;
	
	  @Schema(name = "采集记录")
	  @TableField("RECORDMEMO")
	private String recordmemo;
	
	  @Schema(name = "创建人")
	  @TableField("CREATOR")
	private BigDecimal creator;
	
	  @Schema(name = "创建人姓名")
	  @TableField("CREATNAME")
	private String creatname;
	
	  @Schema(name = "所属部门")
	  @TableField("LINKDEPT")
	private BigDecimal linkdept;
	
	  @Schema(name = "所属公司")
	  @TableField("LINKORG")
	private BigDecimal linkorg;
	  
}
