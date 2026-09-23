package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * 计划管理计划草稿明细
 *
 * @TableName TBL_YQNS_JHGL_JHCG_MX
 */
@TableName(value = "TBL_YQNS_JHGL_JHCG_MX")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsJhglJhcgMx implements Serializable {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 计划草稿审计类型明细主键
     */
    @Schema(name = "计划草稿明细主键")
    @TableId(value = "JHCGMXID", type = IdType.INPUT)
    private BigDecimal jhcgmxid;

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
     * 审计类型
     * 1：专项审计
     * 11：生产经营管理专项审计
     * 12：基建与投资专项审计
     * *************************************
     * 2：经济责任审计
     * 21：二级单位及所属成员单位离任经济责任审计
     * 22：二级单位任中经济责任审计
     * 23: 三级单位离任经济责任审计
     * *************************************
     * 3: 工程建设项目审计
     * 31：工程建设项目结算审计
     * 32：工程建设项目竣工决算审计
     */
    @Schema(name = "审计类型")
    @TableField(value = "SJLX")
    private String sjlx;

    /**
     * 项目名称
     */
    @Schema(name = "项目名称")
    @TableField(value = "XMMC")
    private String xmmc;

    /**
     * 被审计单位
     */
    @Schema(name = "被审计单位")
    @TableField(value = "BSJDW")
    private String bsjdw;

    /**
     * 审计范围
     */
    @Schema(name = "审计范围")
    @TableField(value = "SJFW")
    private String sjfw;

    /**
     * 委任时间
     */
    @Schema(name = "委任时间")
    @TableField(value = "WRSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date wrsj;

    /**
     * 项目类型
     */
    @Schema(name = "项目类型")
    @TableField(value = "XMLX")
    private String xmlx;

    /**
     * 项目数量
     */
    @Schema(name = "项目数量")
    @TableField(value = "XMSL")
    private Long xmsl;

    /**
     * 项目金额
     */
    @Schema(name = "项目金额")
    @TableField(value = "XMJE")
    private BigDecimal xmje;

    /**
     * 单位数量
     */
    @Schema(name = "单位数量")
    @TableField(value = "DWSL")
    private Long dwsl;

    /**
     * 计划草稿主键
     */
    @Schema(name = "计划草稿主键")
    @TableField(value = "JHCGID")
    private Long jhcgid;

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
}