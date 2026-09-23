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
 * 计划需求
 *
 * @TableName TBL_YQNS_JHXQ
 */
@TableName(value = "TBL_YQNS_JHXQ")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsJhxq extends BaseReservedProperty implements Serializable {
    @Bean
    public OracleKeyGenerator genkey() {
        return new OracleKeyGenerator();
    }

    /**
     * 计划需求主键
     */
    @Schema(name = "计划需求主键")
    @TableId(value = "JHXQID")
    private BigDecimal jhxqid;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;

    @Schema(name = "创建人")
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
     * 计划需求名称
     */
    @Schema(name = "计划需求名称")
    @TableField(value = "JHXQMC")
    private String jhxqmc;

    /**
     * 审计项目类型
     * 工程项目审计-预结算审计|竣工决算审计|建设期间审计|项目后评价审计|其他工程审计
     * 经济责任审计-离任审计|任中审计
     * 管理及专项审计-财务收支审计|内部控制审计|绩效审计|信息系统审计|风险管理审计|联合账簿审计|物资采购审计|招投标审计|经济合同审计|科技管理审计|重大政策跟踪审计|金融业务审计|其他审计
     */
    @Schema(name = "工程项目审计-预结算审计|竣工决算审计|建设期间审计|项目后评价审计|其他工程审计    经济责任审计-离任审计|任中审计   管理及专项审计-财务收支审计|内部控制审计|绩效审计|信息系统审计|风险管理审计|联合账簿审计|物资采购审计|招投标审计|经济合同审计|科技管理审计|重大政策跟踪审计|金融业务审计|其他审计")
    @TableField(value = "SJXMLX")
    private String sjxmlx;

    /**
     * 备注
     */
    @Schema(name = "备注")
    @TableField(value = "BZ")
    private String bz;

    /**
     * 项目联系人
     */
    @Schema(name = "项目联系人")
    @TableField(value = "XMLXR")
    private String xmlxr;

    /**
     * 联系电话
     */
    @Schema(name = "联系电话")
    @TableField(value = "LXDH")
    private String lxdh;

    /**
     * 填报单位
     */
    @Schema(name = "填报单位")
    @TableField(value = "TBDW")
    private String tbdw;

    /**
     * 填报时间
     */
    @Schema(name = "填报时间")
    @TableField(value = "TBSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date tbsj;

    /**
     * 组织方式
     */
    @Schema(name = "组织方式")
    @TableField(value = "ZZFS")
    private String zzfs;

    /**
     * 建议审计实施时间
     */
    @Schema(name = "建议审计实施时间")
    @TableField(value = "JYSJSSSJ")
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date jysjsssj;

    /**
     * 审计依据
     */
    @Schema(name = "审计依据")
    @TableField(value = "SJYJ")
    private String sjyj;

    /**
     * 审计目标
     */
    @Schema(name = "审计目标")
    @TableField(value = "SJMB")
    private String sjmb;

    /**
     * 审计范围
     */
    @Schema(name = "审计范围")
    @TableField(value = "SJFW")
    private String sjfw;

    /**
     * 其他情况说明
     */
    @Schema(name = "其他情况说明")
    @TableField(value = "QTQKSM")
    private String qtqksm;

    /**
     * 项目投资渠道
     */
    @Schema(name = "项目投资渠道")
    @TableField(value = "XMTZQD")
    private String xmtzqd;

    /**
     * 概算批复投资金额
     */
    @Schema(name = "概算批复投资金额")
    @TableField(value = "GSPFTZJE")
    private Long gspftzje;

    /**
     * 现已完成投资额
     */
    @Schema(name = "现已完成投资额")
    @TableField(value = "XYWCTZJE")
    private Long xywctzje;

    /**
     * 预计总投资额
     */
    @Schema(name = "预计总投资额")
    @TableField(value = "YJZTZJE")
    private Long yjztzje;

    /**
     * 其中:系统外单位承建额
     */
    @Schema(name = "其中:系统外单位承建额")
    @TableField(value = "QZXTWDWCJE")
    private Long qzxtwdwcje;

    /**
     * 拟竣工验收时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name = "拟竣工验收时间")
    @TableField(value = "NJGYSSJ")
    private Date njgyssj;

    /**
     * 试运行合格时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name = "试运行合格时间")
    @TableField(value = "SYXHGSJ")
    private Date syxhgsj;

    /**
     * 结算完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name = "结算完成时间")
    @TableField(value = "JSWCSJ")
    private Date jswcsj;

    /**
     * 决算完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(name = "决算完成时间")
    @TableField(value = "JUESWCSJ")
    private Date jueswcsj;

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