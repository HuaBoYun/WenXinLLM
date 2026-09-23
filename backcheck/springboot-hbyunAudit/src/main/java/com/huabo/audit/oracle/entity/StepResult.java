package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
  @TableName("TBL_NBSJ_SJMXRESULT")
@Schema(name="StepResult对象")
public class StepResult {
	
	@TableId(value = "RESULTID", type= IdType.INPUT)
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Schema
	private BigDecimal resultid;
	
	private Date savetime;
	private String memo;
	private BigDecimal stepid;//关联模型id
	private Integer source;
	private BigDecimal staffid;//关联用户id
	private String realname;//用户名称
}
