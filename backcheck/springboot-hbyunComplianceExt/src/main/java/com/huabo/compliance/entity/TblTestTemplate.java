package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_COM_EXT_TESTTEMPLE")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblTestTemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    @Id
    @Column(name = "TESTTEMID")
	@Schema(name = "模板主键")
    private BigDecimal testtemid;
    
    @TableField("TEMPLENUMBER")
	@Column(name = "TEMPLENUMBER")
	@Schema(name = "模板编号")
    private String templeNumber;//模板编号
    
    @TableField("TEMPLENAME")
	@Column(name = "TEMPLENAME")
	@Schema(name = "模板名称")
    private String templename;//模板名称
    
    @TableField("MEMO")
	@Column(name = "MEMO")
	@Schema(name = "备注")
    private String memo;
    
    @TableField("STAFFID")
   	@Column(name = "STAFFID")
   	@Schema(name = "创建人")
    private String staffId;
    
    @Transient
    private TblStaff staff;//创建者id
    
    @TableField("CREATEDATETIME")
	@Column(name = "CREATEDATETIME")
	@Schema(name = "创建时间")
    private Date createDateTime;//创建时间
    
    @TableField("TEMPLESTATUS")
	@Column(name = "TEMPLESTATUS")
	@Schema(name = "模板状态")
    private Integer templeStatus;
    
    @TableField("TEMPLEDESC")
	@Column(name = "TEMPLEDESC")
	@Schema(name = "模板说明")
    private String templeDesc;//模板说明
    
    @TableField("SOURCE")
	@Column(name = "SOURCE")
	@Schema(name = "来源：自建、上级公司名称")
    private String source;//来源：自建、上级公司名称
    
    @TableField("TBLCOMANY")
	@Column(name = "TBLCOMANY")
	@Schema(name = "公司Id")
    private String tblComany;//公司名称
    
    @TableField("ISSUED")
   	@Column(name = "ISSUED")
   	@Schema(name = "记录是否下发    是 / ")
       private String issued;//公司名称
}
