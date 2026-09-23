package com.huabo.monitor.oracle.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="TblOrganization对象", description="")
@Table(name = "TBL_ORGANIZATION")
public class TblOrganization implements Serializable {
    public static final Integer AUDITTYPE = 1;
    private static final long serialVersionUID = -5754340813001254466L;
    public static final String WPZJK = "wpzjk";

    @Id
    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
    @TableId("ORGID")
    private BigDecimal orgid;

    @TableField("ORGNAME")
    @ApiModelProperty(value = "公司名称")
    private String orgname;

    @TableField("FATHERORGID")
    private BigDecimal fatherorgid;

    @TableField("ORGNUMBER")
    @ApiModelProperty(value = "公司编号")
    private String orgnumber;

    @TableField("ORGMENO")
    @ApiModelProperty(value = "公司简介")
    private String orgmeno;

    @TableField("MEMO")
    @ApiModelProperty(value = "备注")
    private String memo;

    @TableField("ICODE")
    private String icode;///行业架构ID（在哪个行业下创建行业知识库/行业缺陷库/行业问题库/行业数据库/行业指标库/行业规则库/行业模型库，该字段为哪个行业ID）

    @TableField("ORGTYPE")//是否是公司（普通部门为0，一级公司为1，二级公司为2，
    // 三级公司为3，行业架构为100，行业问题库为101，行业缺陷库为102，行业规则库为103，
    // 行业指标库为104，行业模型库为105，行业知识库为106，审计经验库为107，行业数据库为108）
    private Integer orgtype;

    @TableField("AUDITTYPE")//是否是主责部门/审计部（1是，0否）
    @Column(name = "AUDITTYPE")
    private Integer auditType;

    @TableField("STATUS")//状态（1弃用，0启用）
    private Integer status;

    @TableField("ISZY")//是否开启望远镜
    private String iszy;

    @TableField("HYZSKTYPE")//标识行业知识库的所属模块（风险管控为fxmanage，内部控制为nbkz，智能审计为znsj，智能监控为znjk）
    private String hyzsktype;

    @TableField("ORDERID")//排序编号，用于在显示组织架构排序
    private Integer orderid;

    @TableField("OUTSIDEID") //标识企业来源 为null是本系统，1：蜂信，以后可能为2,3...来表示其它来源   3:华博云注册公司   4:首冠注册用户  5:中财协注册用户  6：大成方略注册用户 7.用友用户
    private Integer outsideid;

    @TableField("OUTSIDEOPENDID")//外部同步企业来源Id
    private String outsideopendid;

    @ApiModelProperty(value = "是否使用自动编号 0 不使用；1 使用")
    @TableField("ISAUTONUMBER")
    private Integer isautonumber;

    @TableField("ORGCREATE")
    private Date orgcreate;

    @TableField("ISINITIALIZATION")// 判断该组织有没有初始化 0初始化 ，
    private Integer isinitialization;

    @ApiModelProperty(value = "职务")
    @TableField("DUTIES")
    private String duties;

    @ApiModelProperty(value = "行业编号")
    @TableField("INDUSTRYID")
    private Integer industryid;

    @ApiModelProperty(value = "新增来源于微信 1为微信 0为pc")
    @TableField("BYWX")
    private String bywx;

    @TableField("DATASOURCE")
    private String datasource;

    @TableField("HISTORYCODE")
    private String historycode;

    @TableField("HISTORYDEPARTMENTID")
    private String historydepartmentid;

    @ApiModelProperty(value = "发文代字")
    @TableField("WRITTENBYDEPT")
    private String writtenByDept;

    @TableField("PKYMORGID")
    @ApiModelProperty(value = "流程平台主键信息")
    private String pkYmOrgId;

    @Transient
    private TblOrganization tblOrganization;
    @Transient
    @JSONField(serialize = false)
    private Set<TblManageRight> tblManageRights = new HashSet(0);

    @JSONField(serialize = false )
    @Transient
    private Set<TblOrganization> children = new HashSet();
}
