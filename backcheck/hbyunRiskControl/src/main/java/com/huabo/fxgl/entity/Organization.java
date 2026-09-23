package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@Data
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
@TableName("TBL_ORGANIZATION")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<Organization> children = new LinkedList<>();

    /**
     * 标识为行业
     */
    public static final int TYPE_HY = 100;
    /**标识为行业问题*/
    public static final int TYPE_HY_WT = 101;
    /**标识为行业缺陷*/
    public static final int TYPE_HY_QX = 102;
    /**标识为行业规则库*/
    public static final int TYPE_HY_GZK = 103;
    /**标识为行业指标库*/
    public static final int TYPE_HY_ZBK = 104;
    /**标识为行业模型库*/
    public static final int TYPE_HY_MXK = 105;
    /**标识为行业知识库库*/
    public static final int TYPE_HY_ZSK = 106;
    /**审计经验库*/
    public static final int TYPE_HY_SJ=107;
    /**行业数据库*/
    public static final int TYPE_HY_SJK=108;
    public static final  Integer ORGTYPE  =100;
    public static final  Integer DEL_YES =1;
    public static final  Integer DEL_NO  =0;
    public static final Integer AUDITTYPE=1;



    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static int getTypeHy() {
        return TYPE_HY;
    }

    public static int getTypeHyWt() {
        return TYPE_HY_WT;
    }

    public static int getTypeHyQx() {
        return TYPE_HY_QX;
    }

    public static int getTypeHyGzk() {
        return TYPE_HY_GZK;
    }

    public static int getTypeHyZbk() {
        return TYPE_HY_ZBK;
    }

    public static int getTypeHyMxk() {
        return TYPE_HY_MXK;
    }

    public static int getTypeHyZsk() {
        return TYPE_HY_ZSK;
    }

    public static int getTypeHySj() {
        return TYPE_HY_SJ;
    }

    public static int getTypeHySjk() {
        return TYPE_HY_SJK;
    }

    public static Integer getORGTYPE() {
        return ORGTYPE;
    }

    public static Integer getDelYes() {
        return DEL_YES;
    }

    public static Integer getDelNo() {
        return DEL_NO;
    }

    public static Integer getAUDITTYPE() {
        return AUDITTYPE;
    }

    public static String getWPZJK() {
        return WPZJK;
    }

    public static String getZC() {
        return ZC;
    }

    public static String getZCX() {
        return ZCX;
    }

    public static String getDCFL() {
        return DCFL;
    }

    /**标识外聘专家库*/
    public static final String WPZJK="wpzjk";

    public static final String ZC="201047";

    public static final String ZCX="7e628e56fbf6b762";

    public static final String DCFL="0687a232942c802e";

    @TableId(type = IdType.INPUT)
	@Schema(name="主键ID")
    private BigDecimal orgid;

    private String orgname;

    private BigDecimal fatherorgid;

    private String orgnumber;

    private String orgmeno;

    private String memo;

    private String icode;

    private BigDecimal orgtype;

    public BigDecimal getOrgtype() {
        return orgtype;
    }

    private BigDecimal audittype;

    public BigDecimal getAudittype() {
        return audittype;
    }

    public void setStatus(BigDecimal status) {
        this.status = status;
    }

    private BigDecimal status;

    private String iszy;

    private String hyzsktype;

    private BigDecimal orderid;

    private BigDecimal outsideid;

    private String outsideopendid;

    /**
     * 是否使用自动编号 0 不使用；1 使用
     */
	@Schema(name="是否使用自动编号 0 不使用；1 使用")
    private BigDecimal isautonumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orgcreate;

    private Long isinitialization;

    /**
     * 职务
     */
	@Schema(name="职务")
    private String duties;

    /**
     * 行业编号
     */
	@Schema(name="行业编号")
    private BigDecimal industryid;

    /**
     * 新增来源于微信 1为微信 0为pc
     */
	@Schema(name="新增来源于微信 1为微信 0为pc")
    private String bywx;

    /**
     * 数据来源;
     */
	@Schema(name="数据来源")
    private String datasource;

    /**
     * 历史id值;
     */
	@Schema(name="历史id值")
    private String historycode;

    /**
     * 珠海港历史部门id
     */
	@Schema(name="珠海港历史部门id")
    private String historydepartmentid;

    private String writtenbydept;

    private String pkymorgid;




}
