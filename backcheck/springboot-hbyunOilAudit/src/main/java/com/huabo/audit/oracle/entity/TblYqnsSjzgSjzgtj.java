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
 * 央企模块-审计整改-审计整改统计
 * @TableName TBL_YQNS_SJZG_SJZGTJ
 */
@TableName(value ="TBL_YQNS_SJZG_SJZGTJ")
@Data
public class TblYqnsSjzgSjzgtj implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
     * 审计整改统计主键
     */
    @Schema(name = "审计整改统计主键")
    @TableId(value = "SJZGTJID",type = IdType.INPUT)
    private BigDecimal sjzgtjid;

    @Schema(name = "问题整改主键")
    @TableField("WTZGID")
    private BigDecimal wtzgid;
    
    @Schema(name = "在报告中对应的编号(审计报告定稿中问题的对应编号)")
    @TableField("ISSUENUMBER")
    private String issueNumber;
    
    @Schema(name = "累计直接经济成果（元）")
    @TableField(value = "LJZJJJCG")
    private BigDecimal ljzjjjcg;
    
    @Schema(name = "累计其他经济成果（元）")
    @TableField(value = "LJQTJJCG")
    private BigDecimal ljqtjjcg;
    
    
    @Schema(name = "外包费用（元）")
    @TableField(value = "OUTCOST")
    private BigDecimal outCost;
    
    /**
     * 制订规章制度数
     */
    @Schema(name = "制订规章制度数")
    @TableField(value = "ZDGZZDS")
    private Integer zdgzzds;

    /**
     * 修订规章制度数
     */
    @Schema(name = "修订规章制度数")
    @TableField(value = "XGGZZDS")
    private String xggzzds;
    
    /**
     * 规章制度情况说明ID
     */
    @Schema(name = "规章制度情况说明ID")
    @TableField(value = "GZZDQKSMID")
    private BigDecimal gzzdqksmid;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField(value = "CJR")
    private String cjr;

    @Schema(name = "创建人")
    @TableField(value = "CJRID")
    private BigDecimal cjrId;
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField(value = "CJSJ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cjsj;


    /**
     * 移送处理事项
     */
    @Schema(name = "移送处理事项")
    @TableField(value = "YSCLSX")
    private String ysclsx;

    /**
     * 其中涉及向司法机关移送或报告事项
     */
    @Schema(name = "其中涉及向司法机关移送或报告事项")
    @TableField(value = "SJSFJGYSBG")
    private String sjsfjgysbg;

    /**
     * 移送处理涉及金额（元）
     */
    @Schema(name = "移送处理涉及金额（元）")
    @TableField(value = "YSCLJE")
    private BigDecimal ysclje;

    /**
     * 其中涉及向司法机关移送或报告事项涉及人员（人
     */
    @Schema(name = "其中涉及向司法机关移送或报告事项涉及人员（人")
    @TableField(value = "SJSFJGYSBGR")
    private String sjsfjgysbgr;

    /**
     * 移送处理人
     */
    @Schema(name = "移送处理人")
    @TableField(value = "YSCLR")
    private String ysclr;

    /**
     * 落实移送处理事项
     */
    @Schema(name = "落实移送处理事项")
    @TableField(value = "LSYSCLSX")
    private String lsysclsx;

    /**
     * 移送处理落实情况（人）
     */
    @Schema(name = "移送处理落实情况（人）")
    @TableField(value = "YSCLLSQK")
    private String yscllsqk;

    /**
     * 其中涉及党纪处分（人）
     */
    @Schema(name = "其中涉及党纪处分（人）")
    @TableField(value = "QZSJDJCF")
    private String qzsjdjcf;

    /**
     * 其中设计政务处分（人）
     */
    @Schema(name = "其中设计政务处分（人）")
    @TableField(value = "QZSJZWCF")
    private String qzsjzwcf;


    /**
     * 其中涉及内部纪律处分（人）
     */
    @Schema(name = "其中涉及内部纪律处分（人）")
    @TableField(value = "QZSJNBJLCF")
    private String qzsjnbjlcf;
    
    @Schema(name = "所属部门主键")
    @TableField(value = "LINKDEPTID")
    private BigDecimal linkDeptId;
    
    @Schema(name = "所属公司主键")
    @TableField(value = "LINKORGID")
    private BigDecimal linkOrgId;

    /**
     * 规章制度情况说明对象实体
     */
    @Schema(name = "规章制度情况说明对象实体")
    @TableField(exist = false)
    private TblYqnsSjzgGzzdqksm tblYqnsSjzgGzzdqksm;

    /**
     * 其他经济处分（人）
     */
    @Schema(name = "其他经济处分（人）")
    @TableField(value = "QTJJCF")
    private String qtjjcf;

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