package com.huabo.fxgl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Data
@TableName("TBL_BUG")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class Bug implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(name="主键ID")
    @TableId(type = IdType.INPUT)
    private BigDecimal bugid;

    /**
     * 缺陷编号
     */
    @Schema(name="缺陷编号")
    private String bugnumber;

    /**
     * 缺陷描述
     */
	@Schema(name="缺陷描述")
    private String bugdescripte;

    /**
     * 发现日期
     */
	@Schema(name="发现日期")
    private LocalDateTime discovertime;

    /**
     * 发现人
     */
	@Schema(name="发现人")
    private String discoverperson;

    /**
     * 缺陷性质
     */
	@Schema(name="缺陷性质")
    private String bugproperty;

    /**
     * 缺陷来源
     */
	@Schema(name="缺陷来源")
    private String bugsource;

    /**
     * 缺陷部门
     */
	@Schema(name="缺陷部门")
    private String bugdepartment;

    /**
     * 是否需要整改
     */
	@Schema(name="是否需要整改")
    private String needreform;

    /**
     * 不整改原因
     */
	@Schema(name="不整改原因")
    private String resonfornoreform;

    /**
     * 状态
     */
	@Schema(name="状态")
    private String bugreformstatus;

	@Schema
    private String projectname;

    /**
     * 备注
     */
	@Schema(name="备注")
    private String memo;

    /**
     * 管理缺陷id
     */
	@Schema(name="管理缺陷id")
    private BigDecimal fatherbugid;

    /**
     * 类别
     */
	@Schema(name="类别")
    private BigDecimal inbugidb;

    /**
     * 所属模块
     */
	@Schema(name="所属模块")
    private String bugbysystem;

    @TableField(exist = false)
    private List<Bug> children;

    /**
     * 业务描述
     */
	@Schema(name="业务描述")
    private String businessdescription;

	@Schema
    private BigDecimal projectid;

	@Schema(name="0缺陷库  1行业")
    @TableField(exist = false)
    private Integer inBugdb;//0缺陷库  1行业

	@Schema
    @TableField(exist = false)
    private String bugdapartment;

	@Schema
    @TableField(exist = false)
    private Set tblInnerrules = new HashSet(0);
    @TableField(exist = false)

	@Schema
    private Set tblOuterrules = new HashSet(0);
    @TableField(exist = false)

	@Schema
    private Set<BugCriterion> tblBugCriterions=new HashSet<BugCriterion>();
    @TableField(exist = false)

	@Schema
    private Set tblAttachments = new HashSet(0);

}
