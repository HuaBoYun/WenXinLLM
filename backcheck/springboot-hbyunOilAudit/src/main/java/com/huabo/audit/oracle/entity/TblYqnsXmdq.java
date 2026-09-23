package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;



/**
 * 项目启动
 *
 * @TableName TBL_YQNS_XMQD
 */
@TableName("TBL_YQNS_XMQD")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsXmdq extends BaseReservedProperty implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	
    @Schema(name = "主键")
    @TableId(value = "XMDQID")
//    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Id
    private BigDecimal xmdqid;

    @Schema(name = "项目名称")
    @TableField(value = "XMNAME")
    private String xmname;



    @Schema(name = "项目类型")
    @TableField(value = "XMTYPE")
    private String xmtype;



    @Schema(name = "实施类型")
    @TableField(value = "SSTYPE")
    private String sstype;


    @Schema(name = "流程类型")
    @TableField(value = "FLOWTYPE")
    private String flowtype;


    @Schema(name = "被审计单位id")
    @TableField(value = "BORGID")
    private BigDecimal borgid;

    @Schema(name = "被审计单位名称")
    @TableField(value = "BORGNAME")
    private String borgname;

    @Schema(name = "实施机构id")
    @TableField(value = "SSORGID")
    private BigDecimal ssorgid;

    @Schema(name = "实施审计机构名称")
    @TableField(value = "SSORGNAME")
    private String ssorgname;

    @Schema(name = "项目经理id")
    @TableField(value = "XMJLSTAFFID")
    private BigDecimal xmjlstaffid;


    @Schema(name = "项目经理名称")
    @TableField(value = "XMJLNAME")
    private String xmjlname;

    @Schema(name = "主审id")
    @TableField(value = "ZSSTAFFID")
    private BigDecimal zsstaffid;

    @Schema(name = "主审名称")
    @TableField(value = "ZSNAME")
    private String zsname;

    @Schema(name = "创建人")
    @TableField(value = "CREATESTAFFID")
    private BigDecimal createstaffid;



    @Schema(name = "创建时间")
    @TableField(value = "CREATEDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdate;

    
    @Schema(name = "现场结束时间")
    @TableField(value = "SITEENDTIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date siteEndTime;
    
    @Schema(name = "状态:1启动 2停止  0未启动")
    @TableField(value = "STATUS")
    private Integer status;
    
    
    @Schema(name = "关联计划id")
    @TableField(value = "PLANID")
    private BigDecimal planid;

    @Schema(name = "关联计划名称")
    @TableField(value = "PLANNAME")
    private String planname;

    
    @Schema(name = "编号")
    @TableField(value = "QDCODE")
    private String qdcode;
    
    
    @Schema(name = "关联计划项目id")
    @TableField(value = "GLJHXMID")
    private BigDecimal gljhxmid;

    
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
    @Schema(name = "关联计划项目类型")
    @TableField(value = "GLJHXMLX")
    private String gljhxmlx;
    
    
    @TableField(value="ISGC")
    @Schema(name="是否工程：0其他项目 1工程项目")
    private String isgc;
    
    // 报表分析字段
    @TableField(exist = false)
    @Schema(name="项目年度")
    private Integer xmnd;
    
    @TableField(exist = false)
    @Schema(name="已完成项目数量")
    private Integer ywcCount;
    
    @TableField(exist = false)
    @Schema(name="实施中项目数量")
    private Integer sszCount;
    
    @TableField(exist = false)
    @Schema(name="项目年度")
    private Integer wqdCount;
    
    @TableField(exist = false)
    @Schema(name="执行率")
    private BigDecimal zxl;
    
    @TableField(exist = false)
    @Schema(name = "副组长主键")
    private BigDecimal fzzStafffId;
    
    @TableField(exist = false)
    @Schema(name = "副组长名称")
    private String fzzName;
    
    @TableField(exist = false)
    @Schema(name = "助审")
    private String assistApprover;
    
    @TableField(exist = false)
    @Schema(name = "助审Id")
    private String assistApproverId;
    
    @Schema(name = "现在开始时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xcsrarttime;

    
    @Schema(name = "现在结束时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xcendtime;
    
    
    /**
     * 组长
     */
    @Schema(name = "组长")
    @TableField(exist = false)
    private String groupLeader;
    
    /**
     * 组长Id
     */
    @Schema(name = "组长Id")
    @TableField(exist = false)
    private BigDecimal groupLeaderId;
    
    @Schema(name = "小组")
    @TableField(exist = false)
    private String auditGroup;
}

