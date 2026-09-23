package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 工作方案表
 *
 * @author wangys
 * @TableName TBL_YQNS_GZFA
 */
@TableName(value = "TBL_YQNS_GZFA")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsGzfa extends BaseReservedProperty implements Serializable {
    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 工作方案表主键
     */ 
    @Schema(name = "工作方案表主键")
    @TableId(value = "GZFAID")
    private BigDecimal gzfaid;

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
     * 项目名称
     */
    @Schema(name = "项目名称")
    @TableField(value = "XMMC")
    private String xmmc;

    /**
     * 计划名称
     */
    @Schema(name = "计划名称")
    @TableField(value = "JHMC")
    private String jhmc;

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
    
    
    @Schema(name = "关联项目启动id")
    @TableField(value = "XMDQID")
    private BigDecimal xmdqid;
    
    @Schema(name = "方案类型：经责、专项、工程")
    @TableField(value = "FALX")
    private String falx;
    
    
    @TableField(exist = false)
    private TblYqnsXmdq xmqd;
    
    
    @Schema(name = "查询参数")
    @TableField(exist = false)
    private String isall;
    
}