package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * 计划管理计划
 *
 * @TableName TBL_YQNS_JHGL_JH
 */
@TableName(value = "TBL_YQNS_JHGL_JH")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsJhglJh extends BaseReservedProperty implements Serializable {

    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 计划主键
     */
    @Schema(name = "计划主键")
    @TableId(value = "JHID", type = IdType.INPUT)
    private BigDecimal jhid;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField(value = "CJSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cjsj;

    @Schema(name = "计划草稿编号")
    @TableField(value = "JHCGNO")
    private String jhcgNo;
    
    @Schema(name = "计划初稿主键")
    @TableField(value = "JHCHUGID")
    private String jhchugid;
    
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
     * 计划名称
     */
    @Schema(name = "计划名称")
    @TableField(value = "JHMC")
    private String jhmc;

    /**
     * 时间
     */
    @Schema(name = "时间")
    @TableField(value = "SJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sj;

    /**
     * 备注
     */
    @Schema(name = "备注")
    @TableField(value = "BZ")
    private String bz;

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

    /**
     * 项目类别：年度计划、新增计划
     */
    @Schema(name = "项目类别")
    @TableField(value = "XMLB")
    private String xmlb;


    @Schema(name = "计划管理计划草稿 关联表")
    @TableField(exist = false)
    private List<TblYqnsJhglJhGL> tblYqnsJhglJhGLList;

    @Schema(name = "专项审计_生产经营管理专项审计")
    @TableField(exist = false)
    private List<ProjectProposalEvaluationEntity> mx11;

    @Schema(name = "专项审计_基建与投资专项审计")
    @TableField(exist = false)
    private List<ProjectProposalEvaluationEntity> mx12;

    @Schema(name = "经济责任审计_二级单位及所属成员单位离任经济责任审计")
    @TableField(exist = false)
    private List<LeaveAudit2LEntity> leaveAudit2LEntityList;

    @Schema(name = "经济责任审计_二级单位任中经济责任审计")
    @TableField(exist = false)
    private List<InterimAuditDetailEntity> auditSuggestion2LEntityList;

    @Schema(name = "经济责任审计_三级单位离任经济责任审计")
    @TableField(exist = false)
    private List<TblYqnsJhglJhGL> leaveAudit3LEntityList;

    /**
     * 工程建设项目审计_工程建设项目结算审计  指定  建设项目基本情况表
     */
    @Schema(name = "工程建设项目审计_工程建设项目结算审计")
    @TableField(exist = false)
    private List<TblYqnsJhglJhGL> tblYqnsJsxmJbqkList;

    /**
     * 工程建设项目审计_工程建设项目竣工决算审计  指定 工程项目竣工验收计划
     */
    @Schema(name = "工程建设项目审计_工程建设项目竣工决算审计")
    @TableField(exist = false)
    private List<TblYqnsJhglJhGL> tblYqnsGcxmjgYsjhList;
    
    @Schema(name = "其他审计项目集合")
    @TableField(exist = false)
    private List<TblYqnsOtherAudit> oauList;

    @Schema(name = "明细主键集合")
    @TableField(exist = false)
    private List<String> mxIds;

    ////////////////////////////////////////////////////////////
    @Schema(name = "立项单位标识")
    @TableField(value = "LXDWID")
    private String lxdwid;


    @Schema(name = "立项单位名称")
    @TableField(value = "LXDWMC")
    private String lxdwmc;


    @Schema(name = "被审计单位标识")
    @TableField(value = "BSJDWID")
    private String bsjdwid;


    @Schema(name = "被审计单位名称")
    @TableField(value = "BSJDWMC")
    private String bsjdwmc;


    @Schema(name = "实施类型")
    @TableField(value = "SSLX")
    private String sslx;

    @Schema(name = "境外项目")
    @TableField(value = "JWXM")
    private String jwxm;

    @Schema(name = "实施审计机构标识")
    @TableField(value = "SSSJJGID")
    private String sssjjgid;

    @Schema(name = "实施审计机构名称")
    @TableField(value = "SSSJJGMC")
    private String sssjjgmc;

    @Schema(name = "项目年度")
    @TableField(value = "XMND")
    private String xmnd;

    @Schema(name = "项目负责处(科)室标识")
    @TableField(value = "XMFZCKSID")
    private String xmfzcksid;

    @Schema(name = "项目负责处(科)室标识名称")
    @TableField(value = "XMFZCKSMC")
    private String xmfzcksmc;

    @Schema(name = "计划实施月份")
    @TableField(value = "JHSSYF")
    private String jhssyf;

    @Schema(name = "审计项目类型")
    @TableField(value = "SSXMLX")
    private String ssxmlx;

    @Schema(name = "立项依据")
    @TableField(value = "LXYJ")
    private String lxyj;

    @Schema(name = "审计项目名称")
    @TableField(value = "SJXMMC")
    private String sjxmmc;

    @Schema(name = "计划类型")
    @TableField(value = "JHLX")
    private String jhlx;

    @Schema(name = "计划投入人日")
    @TableField(value = "JHTRRR")
    private String jhtrrr;

    @Schema(name = "是否对全部经营活动审计")
    @TableField(value = "SFDQBJYHDSJ")
    private String sfdqbjyhdsj;

    @Schema(name = "变更原因")
    @TableField(value = "BGYY")
    private String bgyy;

    @Schema(name = "其他审计主键 逗号拼接字符串")
    @TableField(exist = false,select = false)
    private String otherAuditIdsStrs;
    
    @Schema(name = "计划管理计划草稿 关联表")
    @TableField(exist = false)
    private List<TblYqnsJhglJhcgGL> tblYqnsJhglJhcgGLList;

}

