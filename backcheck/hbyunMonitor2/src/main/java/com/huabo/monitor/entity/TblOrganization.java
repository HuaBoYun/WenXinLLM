package com.huabo.monitor.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;


@Schema(name="TblOrganization对象")
@KeySequence(value = "HIBERNATE_SEQUENCE")
@TableName("TBL_ORGANIZATION")
@Data
public class TblOrganization implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 标识为行业
     */
    public static final int TYPE_HY = 100;
    /**
     * 标识为行业问题
     */
    public static final int TYPE_HY_WT = 101;
    /**
     * 标识为行业缺陷
     */
    public static final int TYPE_HY_QX = 102;
    /**
     * 标识为行业规则库
     */
    public static final int TYPE_HY_GZK = 103;
    /**
     * 标识为行业指标库
     */
    public static final int TYPE_HY_ZBK = 104;
    /**
     * 标识为行业模型库
     */
    public static final int TYPE_HY_MXK = 105;
    /**
     * 标识为行业知识库库
     */
    public static final int TYPE_HY_ZSK = 106;
    /**
     * 审计经验库
     */
    public static final int TYPE_HY_SJ = 107;
    /**
     * 行业数据库
     */
    public static final int TYPE_HY_SJK = 108;
    public static final Integer ORGTYPE = 100;
    public static final Integer DEL_YES = 1;
    public static final Integer DEL_NO = 0;
    public static final Integer AUDITTYPE = 1;
    /**
     * 标识外聘专家库
     */
    public static final String WPZJK = "wpzjk";

    public static final String ZC = "201047";

    public static final String ZCX = "7e628e56fbf6b762";

    public static final String DCFL = "0687a232942c802e";

    @TableId(value = "ORGID",type = IdType.INPUT)
    @Schema(name="主键ID")
    private BigDecimal orgid;

    @Column(name="ORGNAME")
    @Schema(name = "组织名字")
    private String orgname;

    @Column(name="FATHERORGID")
    @Schema(name = "父级id")
    private BigDecimal fatherorgid;

    @Column(name="ORGNUMBER")
    @Schema
    private String orgnumber;
    
    @Column(name="ORGMENO")
    @Schema(name = "注释")
    private String orgmeno;

    @Column(name="MEMO")
    @Schema(name = "注释")
    private String memo;

    @Column(name="ICODE")
    @Schema
    private String icode;

    @Column(name="ORGTYPE")
    @Schema
    private Integer orgtype;
 
    @Column(name="AUDITTYPE")
    @Schema
	private BigDecimal audittype;

    @Column(name="STATUS")
    @Schema(name = "状态")
    private Integer status;
    
    @Column(name="ISZY")
    @Schema
    private String iszy;

    @Column(name="HYZSKTYPE")
    @Schema
    private String hyzsktype;
    @Column(name="ORDERID")
    @Schema
    private BigDecimal orderid;

    @Column(name="OUTSIDEID")
    @Schema
    private BigDecimal outsideid;
    @Column(name="OUTSIDEOPENDID")
    @Schema
    private String outsideopendid;

    /**
     * 是否使用自动编号 0 不使用；1 使用
     */
    @Column(name="ISAUTONUMBER")
    @Schema
    private BigDecimal isautonumber;

    @Column(name="ORGCREATE")
    @Schema
    private Date orgcreate;

    @Column(name="ISINITIALIZATION")
    @Schema
    private Long isinitialization;

    /**
     * 职务
     */

    @Column(name="DUTIES")
    @Schema
    private String duties;

    /**
     * 行业编号
     */
    @Column(name="INDUSTRYID")
    @Schema
    private BigDecimal industryid;

    /**
     * 新增来源于微信 1为微信 0为pc
     */
    @Column(name="BYWX")
    @Schema
    private String bywx;

    /**
     * 数据来源;
     */
    @Column(name="DATASOURCE")
    @Schema
    private String datasource;

    /**
     * 历史id值;
     */
    @Column(name="HISTORYCODE")
    @Schema
    private String historycode;

    /**
     * 珠海港历史部门id
     */
    @Column(name="HISTORYDEPARTMENTID")
    @Schema
    private String historydepartmentid;

    @Column(name="WRITTENBYDEPT")
    @Schema
    private String writtenbydept;
    @Column(name="PKYMORGID")
    @Schema
    private String pkymorgid;



    @JSONField(serialize = false)
    @TableField(exist = false)
    private Set<TblOrganization> children = new HashSet<TblOrganization>();

    @Schema(name="内控-查询评价模板授权页面",hidden=true)
    @TableField(exist=false)
	private List<BigDecimal> getMaxAssorgidList;
    
    
    @Override
    public String toString() {
        return "Organization{" +
                "orgid=" + orgid +
                ", orgname=" + orgname +
                ", fatherorgid=" + fatherorgid +
                ", orgnumber=" + orgnumber +
                ", orgmeno=" + orgmeno +
                ", memo=" + memo +
                ", icode=" + icode +
                ", orgtype=" + orgtype +
                ", audittype=" + audittype +
                ", status=" + status +
                ", iszy=" + iszy +
                ", hyzsktype=" + hyzsktype +
                ", orderid=" + orderid +
                ", outsideid=" + outsideid +
                ", outsideopendid=" + outsideopendid +
                ", isautonumber=" + isautonumber +
                ", orgcreate=" + orgcreate +
                ", isinitialization=" + isinitialization +
                ", duties=" + duties +
                ", industryid=" + industryid +
                ", bywx=" + bywx +
                ", datasource=" + datasource +
                ", historycode=" + historycode +
                ", historydepartmentid=" + historydepartmentid +
                ", writtenbydept=" + writtenbydept +
                ", pkymorgid=" + pkymorgid +
                "}";
    }
}
