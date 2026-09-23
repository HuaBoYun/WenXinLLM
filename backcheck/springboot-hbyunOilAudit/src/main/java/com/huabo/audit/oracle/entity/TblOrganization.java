package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="TblOrganization对象")
@Table(name = "TBL_ORGANIZATION")
public class TblOrganization implements Serializable {
//经办人、申请部门、纠纷承办人
    public static final Integer AUDITTYPE = 1;

    @TableField(exist = false)
	@Transient
	private TblOrganization tblOrganization;

	private static final long serialVersionUID = -5754340813001254466L;

    public static final String WPZJK = "wpzjk";
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @TableId("ORGID")
    private BigDecimal orgid;

    @TableField("ORGNAME")
    @Schema(name = "公司名称")
    private String orgname;

    @TableField("FATHERORGID")
    private BigDecimal fatherorgid;

    @TableField("ORGNUMBER")
    @Schema(name = "公司编号")
    private String orgnumber;

    @TableField("ORGMENO")
    @Schema(name = "公司简介")
    private String orgmeno;

    @TableField("MEMO")
    @Schema(name = "备注")
    private String memo;

    @TableField("ICODE")
    private String icode;

    @TableField("ORGTYPE")
    private Integer orgtype;

    @TableField("AUDITTYPE")
    private Integer auditType;

    @TableField("STATUS")
    private Integer status;

    @TableField("ISZY")
    private String iszy;

    @TableField("HYZSKTYPE")
    private String hyzsktype;

    @TableField("ORDERID")
    private Integer orderid;

    @TableField("OUTSIDEID")
    private Integer outsideid;

    @TableField("OUTSIDEOPENDID")
    private String outsideopendid;

    @Schema(name = "是否使用自动编号 0 不使用；1 使用")
    @TableField("ISAUTONUMBER")
    private Integer isautonumber;

    @TableField("ORGCREATE")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date orgcreate;

    @TableField("ISINITIALIZATION")
    private Integer isinitialization;

    @Schema(name = "职务")
    @TableField("DUTIES")
    private String duties;

    @Schema(name = "行业编号")
    @TableField("INDUSTRYID")
    private Integer industryid;

    @Schema(name = "新增来源于微信 1为微信 0为pc")
    @TableField("BYWX")
    private String bywx;

    @TableField("DATASOURCE")
    private String datasource;

    @TableField("HISTORYCODE")
    private String historycode;

    @TableField("HISTORYDEPARTMENTID")
    private String historydepartmentid;

    @TableField(exist = false)
    @JSONField(serialize = false)
    @Transient
    private Set<TblOrganization> children = new HashSet();

    @TableField(exist = false)
    private List<TblOrganization> children1 = new ArrayList();

}
