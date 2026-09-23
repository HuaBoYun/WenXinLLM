package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

/**
 * 描述:
 * author: tj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_YQNS_LETTER")
@Schema(name="审计承诺书对象") 
public class TblYqnsLetter extends BaseReservedProperty {
	

    @Schema(name = "主键")
    @TableId("LETTERID")
    @Id
    @Column(name = "LETTERID")
    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
    private BigDecimal letterid;

    @TableField(value = "LETTERCODE")
    @Column(name = "LETTERCODE")
    @Schema(name = "编号")
    private String lettercode;

    @TableField(value = "LETTERNAME")
    @Column(name = "LETTERNAME")
    @Schema(name = "名称")
    private String lettername;


    @TableField(value = "CREATESTAFF")
    @Column(name = "CREATESTAFF")
    @Schema(name="创建人id",hidden=true)
    private BigDecimal createstaff;

    @TableField(value = "CREATETIME")
    @Column(name = "CREATETIME")
    @Schema(name="创建时间",hidden=true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @IgnoreSwaggerParameter
    private Date createtime;

    @TableField(value = "PROJECTID")
    @Column(name = "PROJECTID")
    @Schema(name = "所属项目id")
    private BigDecimal projectid;


    @TableField(value = "STATUS")
    @Column(name = "STATUS")
    @Schema(name = "审核状态 1 未审核;2 审核中;3 审核驳回;4 审核完成;5 需调整")
    private Integer status;

    

    @TableField(value = "PROJECTNAME")
    @Column(name = "PROJECTNAME")
    @Schema(name = "项目名称")
    private String projectname;

    
    @TableField(exist = false)
    @Schema(name="拟稿人",hidden=true)
    private String realname;
  
    
}
