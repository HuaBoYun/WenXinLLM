package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 审计项目表
 *
 * @TableName TBL_YQNS_SJXMB
 */
@TableName(value = "TBL_YQNS_SJXMB")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsSjxmb implements Serializable {
    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 审计项目表主键
     */
    @Schema(name = "审计项目表主键")
    @TableId(value = "SJXMBID")
    private BigDecimal sjxmbid;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField(value = "CJSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cjsj;

    /**
     * 扩展字段1
     */
    @Schema(name = "扩展字段1")
    @TableField(value = "EXT1")
    private String ext1;

    /**
     * 扩展字段2
     */
    @Schema(name = "扩展字段2")
    @TableField(value = "EXT2")
    private String ext2;

    /**
     * 扩展字段3
     */
    @Schema(name = "扩展字段3")
    @TableField(value = "EXT3")
    private String ext3;

    /**
     * 组织主键
     */
    @Schema(name = "组织主键")
    @TableField(value = "ORGID")
    private Long orgid;

    /**
     * 更新人
     */
    @Schema(name = "更新人")
    @TableField(value = "GXR")
    private String gxr;

    /**
     * 更新时间
     */
    @Schema(name = "更新时间")
    @TableField(value = "GXSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date gxsj;

    /**
     * 审批状态
     */

    @Schema(name = "审批状态")
    @TableField(value = "SPZT")
    private Long spzt;

    /**
     * 备注
     */
    @Schema(name = "备注")
    @TableField(value = "BZ")
    private String bz;

    /**
     * 合同编号
     */
    @Schema(name = "合同编号")
    @TableField(value = "HTBH")
    private String htbh;

    /**
     * 工程名称
     */
    @Schema(name = "工程名称")
    @TableField(value = "GCMC")
    private String gcmc;

    /**
     * 施工单位
     */
    @Schema(name = "施工单位")
    @TableField(value = "SGDW")
    private String sgdw;

    /**
     * 二审审查金额
     */
    @Schema(name = "二审审查金额")
    @TableField(value = "ESSCJE")
    private BigDecimal esscje;

    /**
     * 本次审计人员
     */
    @Schema(name = "本次审计人员")
    @TableField(value = "BCSJRY")
    private String bcsjry;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Schema(name = "开始时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;

    @Schema(name = "结束时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;

    @Schema(name = "主键集合")
    @TableField(exist = false)
    private List<String> ids;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;

    @Schema(name = "人员集合")
    @TableField(value = "RYIDS")
    private String ryIds;

    @Schema(name = "前端传入人员集合")
    @TableField(exist = false)
    private List<String> ryIdsList;

    @Schema(name = "登录员工ID")
    @TableField(exist = false)
    private String staffLoginId;
    
    @Schema(name = "编号")
    @TableField(value = "XTCODE")
    private String xtcode;


}