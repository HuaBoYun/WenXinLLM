package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 建设项目投资完成情况费用明细表
 * @TableName TBL_YQNS_JSXM_TZWCQK_FYMX
 */
@TableName(value ="TBL_YQNS_JSXM_TZWCQK_FYMX")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsJsxmTzwcqkFymx implements Serializable {
    public OracleKeyGenerator genKey() {
        return new OracleKeyGenerator();
    }

    /**
     * 建设项目投资完成情况费用明细主键
     */
    @Schema(name = "建设项目投资完成情况费用明细主键")
    @TableId(value = "JSXMTZWCQKFYMXID",type = IdType.INPUT)
    private Long jsxmtzwcqkfymxid;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField(value = "CJSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cjsj;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;

    /**
     * 更新时间
     */
    @Schema(name = "更新时间")
    @TableField(value = "GXSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date gxsj;

    /**
     * 更新人
     */
    @Schema(name = "更新人")
    @TableField(value = "GXR")
    private String gxr;

    /**
     * 合同编号
     */
    @Schema(name = "合同编号")
    @TableField(value = "HTBH")
    private String htbh;

    /**
     * 费用类型（工程费用|其他费用）
     */
    @Schema(name = "费用类型（工程费用|其他费用）")
    @TableField(value = "FYLX")
    private String fylx;

    /**
     * 建设项目投资完成情况主键
     */
    @Schema(name = "建设项目投资完成情况主键")
    @TableField(value = "JSXMTZWCQKID")
    private Long jsxmtzwcqkid;

    /**
     * 备注
     */
    @Schema(name = "备注")
    @TableField(value = "BZ")
    private String bz;

    /**
     * 投资节超情况说明
     */
    @Schema(name = "投资节超情况说明")
    @TableField(value = "TZJCQKSM")
    private String tzjcqksm;

    /**
     * 结算金额
     */
    @Schema(name = "结算金额")
    @TableField(value = "JSJE")
    private BigDecimal jsje;

    /**
     * 批复概算投资
     */
    @Schema(name = "批复概算投资")
    @TableField(value = "PFGSTZJE")
    private BigDecimal pfgstzje;

    /**
     * 工程或费用名称
     */
    @Schema(name = "工程或费用名称")
    @TableField(value = "GCHFYMC")
    private String gchfymc;

    /**
     * 投资节超（概算-实际完成）
     */
    @Schema(name = "投资节超（概算-实际完成）")
    @TableField(value = "TZJC")
    private String tzjc;

    /**
     * 合同金额
     */
    @Schema(name = "合同金额")
    @TableField(value = "HTJE")
    private BigDecimal htje;

    /**
     * 计划文号
     */
    @Schema(name = "计划文号")
    @TableField(value = "JHWH")
    private String jhwh;

    /**
     * 实施单位
     */
    @Schema(name = "实施单位")
    @TableField(value = "SSDW")
    private String ssdw;

    /**
     * 审批状态
     */
    @Schema(name = "审批状态")
    @TableField(value = "SPZT")
    private Long spzt;



    /**
     * 组织
     */
    @Schema(name = "组织")
    @TableField(value = "ORGID")
    private Long orgid;

    /**
     * 扩展字段
     */
    @Schema(name = "扩展字段")
    @TableField(value = "EXT3")
    private String ext3;

    /**
     * 扩展字段2
     */
    @Schema(name = "扩展字段2")
    @TableField(value = "EXT2")
    private String ext2;

    /**
     * 扩展字段1
     */
    @Schema(name = "扩展字段1")
    @TableField(value = "EXT1")
    private String ext1;

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

    @Schema(name = "实体主键集合")
    @TableField(exist = false)
    private List<String> ids;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件对象集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;
}