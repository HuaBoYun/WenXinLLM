package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;



/**
 * 项目延期申请
 *
 * @TableName TBL_YQNS_XMYQSQ
 */
@TableName("TBL_YQNS_XMYQSQ")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsXmsqsq extends BaseReservedProperty implements Serializable {

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

    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;


    @Schema(name = "被审计单位id")
    @TableField(value = "BORGID")
    private String borgid;

    @Schema(name = "被审计单位名称")
    @TableField(value = "BORGNAME")
    private String borgname;

    @Schema(name = "实施机构id")
    @TableField(value = "SSORGID") 
    private BigDecimal ssorgid;

    @Schema(name = "实施审计机构名称")
    @TableField(value = "SSORGNAME")
    private String ssorgname;

    @Schema(name = "组长id")
    @TableField(value = "ZZSTAFFID")
    private BigDecimal zzstaffid;


    @Schema(name = "组长名称")
    @TableField(value = "ZZNAME")
    private String zzname;
    
    @Schema(name = "组员id")
    @TableField(value = "ZYSTAFFID")
    private String zystaffid;


    @Schema(name = "组员名称")
    @TableField(value = "ZYNAME")
    private String zyname;

    @Schema(name = "主审id")
    @TableField(value = "ZSSTAFFID")
    private BigDecimal zsstaffid;

    @Schema(name = "主审名称")
    @TableField(value = "ZSNAME")
    private String zsname;

    
    @Schema(name = "延期原因")
    @TableField(value = "YQYY")
    private String yqyy;
     
    
    
    @Schema(name = "延期时间")
    @TableField(value = "SSDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ssdate;
    
    @Schema(name = "延期时间")
    @TableField(value = "YQDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date yqdate;
    
    @Schema(name = "关联项目id")
    @TableField(value = "PROJECTID")
    private BigDecimal projectid;

    @Schema(name = "关联项目名称")
    @TableField(value = "PROJCTNAME")
    private String projctname;
    
    @Schema(name = "创建人")
    @TableField(value = "CREATESTAFFID")
    private BigDecimal createstaffid;



    @Schema(name = "创建时间")
    @TableField(value = "CREATEDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdate;

    @Schema(name = "编号")
    @TableField(value = "XMBH")
    private String xmbh;

    
    @Schema(name = "状态")
    @TableField(value = "STATUS")
    private Integer status;
}

