package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 央企模块-审计整改-移送内容
 * @TableName TBL_YQNS_SJZG_YSNR
 */
@TableName(value ="TBL_YQNS_SJZG_YSNR")
@Data
public class TblYqnsSjzgYsnr implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * 移送内容主键
     */
    @Schema(name = "移送内容主键")
    @TableId(value = "YSNRID",type = IdType.INPUT)
    private BigDecimal ysnrid;

    /**
     * 移送处理事项
     */
    @Schema(name = "移送处理事项")
    @TableField(value = "YSCLSX")
    private Integer ysclsx;

    /**
     * 其中涉及向司法机关移送或报告事项
     */
    @Schema(name = "其中涉及向司法机关移送或报告事项")
    @TableField(value = "SJSFJGYSSX")
    private String sjsfjgyssx;

    /**
     * 移送处理涉及金额
     */
    @Schema(name = "移送处理涉及金额")
    @TableField(value = "YSCLSJJE")
    private BigDecimal ysclsjje;

    /**
     * 移送处理人员
     */
    @Schema(name = "移送处理人员")
    @TableField(value = "YSCLR")
    private Integer ysclr;

    /**
     * 其中涉及向司法机关移送或报告事项涉及人员
     */
    @Schema(name = "其中涉及向司法机关移送或报告事项涉及人员")
    @TableField(value = "SJSFJGYSSXR")
    private String sjsfjgyssxr;

    /**
     * 落实移送处理事项
     */
    @Schema(name = "落实移送处理事项")
    @TableField(value = "LSYSCLSX")
    private Integer lsysclsx;

    /**
     * 移送处理落实情况
     */
    @Schema(name = "移送处理落实情况(人)")
    @TableField(value = "YSCLLSQK")
    private Integer yscllsqk;

    /**
     * 其中涉及党纪处分
     */
    @Schema(name = "其中涉及党纪处分")
    @TableField(value = "SJDJCF")
    private String sjdjcf;

    /**
     * 其中涉及政务处分
     */
    @Schema(name = "其中涉及政务处分")
    @TableField(value = "SJZWCF")
    private String sjzwcf;

    /**
     * 其中涉及内部纪律处分
     */
    @Schema(name = "其中涉及内部纪律处分")
    @TableField(value = "SJNBJLCF")
    private String sjnbjlcf;

    /**
     * 其他经济处分
     */
    @Schema(name = "其他经济处分")
    @TableField(value = "QTJJCF")
    private String qtjjcf;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;
    
    @Schema(name = "创建人主键")
    @TableField(value = "CJRID")
    private BigDecimal cjrId;
    
    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> ysAttIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private List<TblAttachment> ysAttList;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField(value = "CJSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cjsj;
    
    /**
     * 当期直接经济成果类型
     */
    @Schema(name = "当期直接经济成果类型")
    @TableField(exist = false)
    private String dqzjjjcgtype;
    
    /**
     * 当期直接经济成果（元）
     */
    @Schema(name = "当期直接经济成果（元）")
    @TableField(exist = false)
    private BigDecimal dqzjjjcg;
    
    /**
     * 当期其他经济成果类型
     */
    @Schema(name = "当期其他经济成果类型")
    @TableField(exist = false)
    private String dqqtjjcgtype;
    
    /**
     * 当期其他经济成果（元）
     */
    @Schema(name = "当期其他经济成果（元）")
    @TableField(exist = false)
    private BigDecimal dqqtjjcg;

}