package com.huabo.system.entity;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_FLOW_BUSSINESS")
@Schema(name = "TblFlowBussiness")
@TableName(value = "TBL_FLOW_BUSSINESS")
public class TblFlowBussiness implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId(value="BUSSINESSID",type = IdType.INPUT)
    private BigDecimal bussinessid;
    @TableField("BUSSINESSNUMBER")
    @Schema(name="业务编号")
    private String bussinessnumber;
    @TableField("BUSSINESSNAME")
    @Schema(name="业务名称")
    private String bussinessname;//业务名称
    @Schema(name="业务描述")
    @TableField("BUSSINESSDES")
    private String bussinessdes;//业务描述
    @TableField("FLOWID")
    @Schema(name="所属流程主键")
    private BigDecimal flowid;
}
