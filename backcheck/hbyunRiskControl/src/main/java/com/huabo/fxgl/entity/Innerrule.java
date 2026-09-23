package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@TableName("TBL_INNERRULE")
public class Innerrule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
	@Schema(name="主键ID")
    private BigDecimal innrulid;

	@Schema(name="制度名称")
    private String rulename;

	@Schema(name="发文机构")
	@TableField(value = "PUBLISHORG")
    private String publishorg;
	
	//@Transient
	@Schema(name="机构名称")
    private String orgname;

	@Schema(name="发文机构名称")
	//@Transient
    @TableField(value = "publishorgname", exist = false)
    private String publishorgname;

	@Schema(name="发文日期")
    private LocalDateTime publishdate;

	@Schema(name="发文文号")
    private String rulenumber;

	@Schema(name="内规状态")
	@TableField(value = "STATUS")
    private String status;

	@Schema(name="时效性")
	@TableField(value = "TIMELINESS")
    private String timeliness;

	@Schema(name="制度编号")
	@TableField(value = "RULECODE")
    private String rulecode;

	@Schema(name="发文正文")
	@TableField(value = "BODYINFO")
    private String bodyinfo;
	
	@Schema(name="类别")
	@TableField(value = "INNRULETYPE")
    private String innruletype;
	
	@Schema(name="版本")
	//@Transient
    private String version;
	
	@Schema(name="无意义")
	//@Transient
    private String memo;
	
	@Schema(name="无意义")
	//@Transient
    private String hostdepartment;

	@Schema(name="无意义")
	//@Transient
    private String coorganizer;

	@Schema(name="无意义")
	//@Transient
    private LocalDateTime takeeffecttime;

	@Schema(name="无意义")
	//@Transient
    private String summaryinfo;

	@Schema(name="无意义")
	//@Transient
    private String enteringperson;

	@Schema(name="无意义")
	//@Transient
    private LocalDateTime enteringtime;

}
