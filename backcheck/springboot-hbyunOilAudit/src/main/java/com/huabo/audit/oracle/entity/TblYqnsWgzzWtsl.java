package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.huabo.audit.oracle.entity.base.ReservedEntity;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_WGZZ_WTSL")
@Data
@Schema(name="问题线索受理")
@Accessors(chain = true)
public class TblYqnsWgzzWtsl extends ReservedEntity {

    @TableField(value = "ID")
    @Schema(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal id;
    
    @TableField(value = "SLNUMBER")
    @Schema(name = "编号")
    private String slnumber;

    @TableField(value = "SLNAME")
    @Schema(name = "名称")
    private String slname;
    
    @TableField("MEMO")
	@Schema(name = "描述")
	private String memo;

    @TableField("STATUS")
    @Schema(name = "状态")
    private Integer status;
    
    @TableField("CREATETIME")
    @Schema(name = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createtime;

    @TableField("CREATOR")
    @Schema(name = "创建人")
    private BigDecimal creator;
    
    //创建人姓名
  	@Transient
    private String createstaffname;

    @Transient
    private String attIds;
    
    @TableField(value = "FILUE")
    @Schema(name = "附件")
    private String filue;
    
    @TableField(value = "ISUSE")
	@Schema(name = "是否已引用")
	private Integer isuse;

}
