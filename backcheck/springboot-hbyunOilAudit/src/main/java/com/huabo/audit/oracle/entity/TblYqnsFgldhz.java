package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 分管领导汇总表
 *
 * @author wystan
 * @TableName TBL_YQNS_FGLDHZ
 */
@TableName(value = "TBL_YQNS_FGLDHZ")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsFgldhz implements Serializable {
    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 分管领导汇总主键
     */
    @Schema(name = "分管领导汇总主键")
    @TableId(value = "FGLDHZID")
    private BigDecimal fgldhzid;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;
    
    @Schema(name = "创建人")
    @TableField(value = "NO")
    private String no;

    @Schema(name = "创建人")
    @TableField(value = "CJRXM")
    private String cjrxm;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(value = "GXSJ")
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
     * 分管领导主键
     */
    @Schema(name = "公司领导主键")
    @TableField(value = "GSLDID")
    private String gsldid;

    /**
     * 分管领导姓名
     */
    @Schema(name = "公司领导姓名")
    @TableField(value = "GSLDXM")
    private String gsldxm;

    /**
     * 立项要求
     */
    @Schema(name = "立项要求")
    @TableField(value = "LXYQ")
    private String lxyq;

    /**
     * 落实建议
     */
    @Schema(name = "落实建议")
    @TableField(value = "LSJY")
    private String lsjy;

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

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private Integer status;

    @Schema(name = "主键集合")
    @TableField(exist = false)
    private List<String> ids;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private List<String> attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private List<TblAttachment> attachments;

    @TableField(value="PERSON_IDS")
    @Schema(name="下发的人员ID")
    private String personIds;
    
    
    @TableField(exist = false)
    @Schema(name="创建年度")
    private String createYear;


}