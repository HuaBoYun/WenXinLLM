package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Id;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
@TableName("TBL_TESTELEMENT")
@Schema(name="TblTestelement对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
@Data
public class TblTestelement extends FlexibleFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    @Id
    @Column(name = "ELEMENTID")
	@Schema(name = "元素主键")
    private BigDecimal elementid;

    @TableField("RISKTYPE")
   	@Column(name = "RISKTYPE")
   	@Schema(name = "风险描述")
    private String risktype;//风险描述

    @TableField("CHECKMETHOD")
   	@Column(name = "CHECKMETHOD")
   	@Schema(name = "检查方法")
    private String checkmethod;//检查方法

    @TableField("CONTROLMETHOD")
   	@Column(name = "CONTROLMETHOD")
   	@Schema(name = "控制方法")
    private String controlmethod;//控制方法

    @TableField("CONTROLTYPE")
   	@Column(name = "CONTROLTYPE")
   	@Schema(name = "控制类型")
    private String controltype;//控制类型

    @TableField("CONTROLREQ")
   	@Column(name = "CONTROLREQ")
   	@Schema(name = "控制频率")
    private String controlreq;//控制频率

    @TableField("MATERIAL")
   	@Column(name = "MATERIAL")
   	@Schema(name = "检查材料")
    private String material;//检查材料

    @TableField("ELEMENTCODE")
   	@Column(name = "ELEMENTCODE")
   	@Schema(name = "编号")
    private String elementcode;//编号

    @TableField("BUSINESSDESC")
   	@Column(name = "BUSINESSDESC")
   	@Schema(name = "业务描述")
    private String businessdesc;//业务描述

    @TableField("CONTROLTARGET")
   	@Column(name = "CONTROLTARGET")
   	@Schema(name = "控制目标")
    private String controltarget;//控制目标

    @TableField("CONTROLMEASURES")
   	@Column(name = "CONTROLMEASURES")
   	@Schema(name = "控制措施")
    private String controlmeasures;//控制措施

    @TableField("CREATETIME")
   	@Column(name = "CREATETIME")
   	@Schema(name = "创建时间")
    private LocalDateTime createtime;
    
    @TableField("TYPEID")
   	@Column(name = "TYPEID")
   	@Schema(name = "类型主键")
    private BigDecimal typeid;

    @TableField("TEMPLID")
   	@Column(name = "TEMPLID")
   	@Schema(name = "测试模板主键")
    private BigDecimal templid;
    
    
    
    @TableField("LONGSTRING1")
   	@Column(name = "LONGSTRING1")
   	@Schema(name = "内部控制基本规范、应用指引和解读相关要求")
    private String longString1;
    
    
    @TableField("LONGSTRING2")
   	@Column(name = "LONGSTRING2")
   	@Schema(name = "标准化控制+中核集团内部控制评价补充要求")
    private String longString2;

}
