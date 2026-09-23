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
@TableName("TBL_YQNS_WGZZ_WTHC")
@Data
@Schema(name="问题核查")
@Accessors(chain = true)
public class TblYqnsWgzzWthc extends ReservedEntity {

    @TableField(value = "ID")
    @Schema(name = "id")
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal id;
    
	@TableField(value = "EDITORGID")
	@Schema(name = "填报单位")
	private BigDecimal editorgid;
	
	//填报单位名称
	@Transient
    private String editorgname;

	@TableField("EDITTIME")
    @Schema(name = "编制时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date edittime;

    @TableField(value = "HCNUMBER")
    @Schema(name = "编号")
    private String hcnumber;

    @TableField(value = "HCNAME")
    @Schema(name = "名称")
    private String hcname;

    @TableField("STATUS")
    @Schema(name = "状态")
    private Integer status;

    @TableField("CREATOR")
    @Schema(name = "编制人")
    private BigDecimal creator;
    
    //编制人姓名
  	@Transient
    private String editstaffname;

    @Transient
    private String attIds;
    
    @TableField(value = "FILUE")
    @Schema(name = "附件")
    private String filue;

	@TableField("CONTENT")
	@Schema(name = "富文本")
	private String content;
	
	@TableField(value = "SLID")
    @Schema(name = "问题线索受理ID")
    private BigDecimal slid;
	
	@TableField(value = "ISUSE")
	@Schema(name = "是否已引用")
	private Integer isuse;
	
}
