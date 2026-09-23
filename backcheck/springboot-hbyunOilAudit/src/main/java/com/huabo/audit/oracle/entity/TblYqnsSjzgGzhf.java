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
import java.util.Date;
import java.util.List;

/**
 * 央企模块-审计整改-跟踪回访
 * @TableName TBL_YQNS_SJZG_GZHF
 */
@TableName(value ="TBL_YQNS_SJZG_GZHF")
@Data
@KeySequence(value = "HIBERNATE_SEQUENCE")
public class TblYqnsSjzgGzhf implements Serializable {

    @Bean
    public OracleKeyGenerator genKey() {
        return new OracleKeyGenerator();
    }

    /**
     * 跟踪回访主键
     */
    @Schema(name = "跟踪回访主键")
    @TableId(value = "GZHFID",type = IdType.INPUT)
    private Long gzhfid;

    /**
     * 项目标准化编码
     */
    @Schema(name = "项目标准化编码")
    @TableField(value = "XMBZHBM")
    private String xmbzhbm;

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
     * 项目ID
     */
    @Schema(name = "项目ID")
    @TableField(value = "XMID")
    private Long xmid;

    /**
     * 立项单位组织机构标准化编码
     */
    @Schema(name = "立项单位组织机构标准化编码")
    @TableField(value = "LXDWZZJGBM")
    private String lxdwzzjgbm;

    /**
     * 立项单位
     */
    @Schema(name = "立项单位")
    @TableField(value = "LXDW")
    private String lxdw;

    /**
     * 项目名称
     */
    @Schema(name = "项目名称")
    @TableField(value = "XMMC")
    private String xmmc;

    /**
     * 问题金额（元）
     */
    @Schema(name = "问题金额（元）")
    @TableField(value = "WTJE")
    private Float wtje;

    /**
     * 审减金额（元）
     */
    @Schema(name = "审减金额（元）")
    @TableField(value = "SJJE")
    private Float sjje;
    /**
     * 整改时限
     */
    @Schema(name = "整改时限")
    @TableField(value = "ZGSX")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date zgsx;

    /**
     * 处理意见或整改建议
     */
    @Schema(name = "处理意见或整改建议")
    @TableField(value = "CLYJ")
    private String clyj;

    /**
     * 整改督促牵头部门或单位
     */
    @Schema(name = "整改督促牵头部门或单位")
    @TableField(value = "ZGDCQTBM")
    private String zgdcqtbm;

    /**
     * 整改状态
     */
    @Schema(name = "整改状态")
    @TableField(value = "ZGZT")
    private String zgzt;

    /**
     * 整改金额
     */
    @Schema(name = "整改金额")
    @TableField(value = "ZGJE")
    private Float zgje;


    /**
     * 问题定性编号
     */
    @Schema(name = "问题定性编号")
    @TableField(value = "WTDXBH")
    private String wtdxbh;

    /**
     * 整改描述
     */
    @Schema(name = "整改描述")
    @TableField(value = "ZGMS")
    private String zgms;

    /**
     * 报表年度
     */
    @Schema(name = "报表年度")
    @TableField(value = "BBND")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date bbnd;

    /**
     * 二级单位
     */
    @Schema(name = "二级单位")
    @TableField(value = "EJDW")
    private String ejdw;

    /**
     * 是否统计金额
     */
    @Schema(name = "是否统计金额")
    @TableField(value = "SFTJJE")
    private String sftjje;

    /**
     * 项目性质
     */
    @Schema(name = "项目性质")
    @TableField(value = "XMXZ")
    private String xmxz;

    /**
     * 是否境外
     */
    @Schema(name = "是否境外")
    @TableField(value = "SFJW")
    private String sfjw;

    /**
     * 直接经济成果
     */
    @Schema(name = "直接经济成果")
    @TableField(value = "ZJJJCG")
    private String zjjjcg;

    /**
     * 定性法规依据
     */
    @Schema(name = "定性法规依据")
    @TableField(value = "DXFGYJ")
    private String dxfgyj;

    /**
     * 是否移送
     */
    @Schema(name = "是否移送")
    @TableField(value = "SFYS")
    private String sfys;

    /**
     * 移送内容ID
     */
    @Schema(name = "移送内容ID")
    @TableField(value = "YSNRID")
    private Long ysnrid;


    @Schema(name = "移送内容")
    @TableField(exist = false)
    private TblYqnsSjzgYsnr tblYqnsSjzgYsnr;

    @Schema(name="开始时间",hidden=true)
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;

    @Schema(name="结束时间",hidden=true)
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
    @IgnoreSwaggerParameter
    private List<TblAttachment> attachments;
}