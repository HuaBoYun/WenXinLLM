package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.fxgl.util.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@Data
@TableName("TBL_OUTERRULE")
public class Outerrule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
	@Schema(name="主键ID")
    private BigDecimal outrulid;

	@Schema(name="制度名称")
    private String rulename;

	@Schema(name="发文机构")
	 @TableField(value = "publishorg")
    private String publishorg;

	@Schema(name="公司名称")
    private String orgname;

	@Schema(name="发文日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date publishdate;

	@Schema(name="发文文号")
    private String rulenumber;

	@Schema
    private String memo;

	@Schema
    private String outruletype;

	@Schema
    private String effectivelevel;

	@Schema(name="时效性")
    private String timeliness;

	@Schema
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime takeeffecttime;

	@Schema
    private String summaryinfo;

	@Schema
    private String enteringperson;

	@Schema
    private LocalDateTime enteringtime;

	@Schema
    private String status;

	@Schema
//    @TableField(select = false)
    private String bodyinfo;

	@Schema
    private String rulecode;

	@Schema(name="所属公司")
    private BigDecimal createorgid;

	@Schema
    private String applyarea;

	@Schema
    private BigDecimal orgid;

	@Schema(name="是否是python 外部爬取文件  1-是 0-否")
    private String ispythonflag;

	@Schema
    @TableField(exist = false)
	@IgnoreSwaggerParameter
    private Organization Organization;



}
