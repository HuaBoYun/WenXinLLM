package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name="TBL_NBSJ_AUDITSTEP")
@Schema(name="审计步骤体类对象")
public class TblNbsjAuditStepEntity {
//	private static final long serialVersionUID = 1L;
	
	@TableId(value = "stepid", type= IdType.AUTO)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Schema
    private BigDecimal stepid;
	
	@TableField(value = "stepno")
    @Schema(name = "模型编号")
    private String stepno;
	
	@TableField(value = "steptitle")
    @Schema(name = "模型名称")
    private String steptitle;
	
	@TableField(value = "stepcontent")
    @Schema(name = "步骤内容")
    private String stepcontent;
	
	@TableField(value = "createtime")
    @Schema(name="创建时间",hidden=true)
    private Date createtime;

    @TableField(value = "updatetime")
    @Schema(name="修改时间",hidden=true)
    private Date updatetime;
	
	@TableField(value = "TYPEID")
    @Schema(name = "关联分类目录id")
    private BigDecimal typeid;
	
	@TableField(value = "SQLSTR")
    @Schema(name = "执行SQL")
    private String sqlstr;
	
	
	@TableField(value = "CREATESTAFFID")
    @Schema(name = "创建人id")
    private BigDecimal createstaffid;
	
	@TableField(value = "CREATENAME")
	@Schema(name = "创建人名称")
    private String createname;
	
	
	@TableField(value = "orgid")
    @Schema(name="创建公司",hidden=true)
    private BigDecimal orgid;
	
	
	@TableField(value = "BOOKID")
	@Schema(name = "关联账簿id")
    private String bookid;
	
	@Schema(name = "关联账簿")
    private String acctid;
	
}
