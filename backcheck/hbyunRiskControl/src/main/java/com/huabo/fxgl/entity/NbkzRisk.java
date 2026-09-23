package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-09
 */
@Data
@TableName("TBL_NBKZ_RISK")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class NbkzRisk implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.INPUT)
	@Schema(name="ID")
    private BigDecimal riskid;

    /**
     * 风险编号
     */
	@Schema(name="风险编号")
    private String risknumber;

    /**
     * 风险名称
     */
	@Schema(name="风险名称")
    private String riskname;

    /**
     * 发生日期
     */
	@Schema(name="发生日期")
    private LocalDateTime occureddate;

    /**
     * 发现日期
     */
	@Schema(name="发现日期")
    private LocalDateTime discovereddate;

    /**
     * 创建日期
     */
	@Schema(name="创建日期")
    private LocalDateTime createdate;

    /**
     * 责任部门
     */
	@Schema(name="责任部门")
//    private BigDecimal orgid;
    @TableField(value = "ORGID", property = "tblOrganiDem.orgid")
    private Organization tblOrganiDem;

	@Schema
    private BigDecimal orgid;

    /**
     * 所属模块
     */
	@Schema(name="所属模块")
    private String stype;

	@Schema
    private BigDecimal createorid;

    /**
     * 相关部门
     */
	@Schema(name="相关部门")
    private String sysorgid;

    /**
     * 风险描述
     */
	@Schema(name="风险描述")
    private String riskdes;

    /**
     * 事件说明
     */
	@Schema(name="事件说明")
    private String riskeventdescription;

    /**
     * 损失事件定性类别
     */
	@Schema(name="损失事件定性类别")
    private String losseventcategory;

	@Schema
    @TableField(value = "CREATEORID", property = "tblStaff.staffid")
    private Staff tblStaff;

    @TableField(exist = false)
    private Set attachments=new HashSet(0);



}
