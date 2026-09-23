package com.huabo.system.vo.result;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
@Data
@Schema(name="组织信息返回对象", description="组织信息返回对象")
@Table(name = "TBL_ORGANIZATION")
public class TblOrganizationResult implements Serializable {

    @Column(name = "ORGID")
    @Schema(name="组织主键")
    private BigDecimal orgid;

    @Column(name = "ORGNAME")
    @Schema(name="公司名称")
    private String orgname;

    @Column(name = "FATHERORGID")
    @Schema(name="父级公司主键")
    private BigDecimal fatherorgid;

    @Column(name = "ORGNUMBER")
    @Schema(name="公司编号")
    private String orgnumber;

    @Column(name = "ORGMENO")
    @Schema(name="公司简介")
    private String orgmeno;

    @Column(name = "MEMO")
    @Schema(name="备注")
    private String memo;

    @Column(name = "ICODE")
    @Schema(name="行业架构ID（在哪个行业下创建行业知识库/行业缺陷库/行业问题库/行业数据库/行业指标库/行业规则库/行业模型库，该字段为哪个行业ID）")
    private String icode;///行业架构ID（在哪个行业下创建行业知识库/行业缺陷库/行业问题库/行业数据库/行业指标库/行业规则库/行业模型库，该字段为哪个行业ID）

    // 三级公司为3，行业架构为100，行业问题库为101，行业缺陷库为102，行业规则库为103，
    // 行业指标库为104，行业模型库为105，行业知识库为106，审计经验库为107，行业数据库为108）
    @Column(name = "ORGTYPE")
    @Schema(name="组织类型 0- 部门 、1-100 公司 ，100以上行业架构")
    private Integer orgtype;

    @Column(name = "AUDITTYPE")
    @Schema(name="是否是主责部门/审计部（1是，0否）")
    private Integer auditType;

    @Column(name = "STATUS")
    @Schema(name="状态（1弃用，0启用）")
    private Integer status;

    @Column(name = "ISZY")
    @Schema(name="是否开启望远镜")
    private String iszy;

    @Column(name = "HYZSKTYPE")
    @Schema(name="标识行业知识库的所属模块（风险管控为fxmanage，内部控制为nbkz，智能审计为znsj，智能监控为znjk）")
    private String hyzsktype;

    @Column(name = "ORDERID")
    @Schema(name="排序编号，用于在显示组织架构排序")
    private Integer orderid;

    @Column(name = "OUTSIDEID")
    @Schema(name="标识企业来源 为null是本系统，1：蜂信，以后可能为2,3...来表示其它来源   3:华博云注册公司   4:首冠注册用户  5:中财协注册用户  6：大成方略注册用户 7.用友用户")
    private Integer outsideid;

    @Column(name = "OUTSIDEOPENDID")
    @Schema(name="外部同步企业来源Id")
    private String outsideopendid;

    @Schema(name="是否使用自动编号 0 不使用；1 使用")
    @Column(name = "ISAUTONUMBER")
    private Integer isautonumber;

    @Column(name = "ORGCREATE")
    @Schema(name="创建时间")
    private Date orgcreate;

    @Column(name = "ISINITIALIZATION")
    @Schema(name=" 判断该组织有没有初始化 0初始化 ，")
    private Integer isinitialization;

    @Schema(name="职务")
    @Column(name = "DUTIES")
    private String duties;

    @Schema(name="行业编号")
    @Column(name = "INDUSTRYID")
    private Integer industryid;

    @Schema(name="新增来源于微信 1为微信 0为pc")
    @Column(name = "BYWX")
    private String bywx;

    @Schema(name="数据来源")
    @Column(name = "DATASOURCE")
    private String datasource;

    @Schema(name="外部同步数据主键")
    @Column(name = "HISTORYCODE")
    private String historycode;

    @Schema(name="外部同步父级主职主键")
    @Column(name = "HISTORYDEPARTMENTID")
    private String historydepartmentid;
    
    @Schema(name="发文代字")
    @Column(name = "WRITTENBYDEPT")
    private String writtenByDept;

    @TableField("PKYMORGID")
    @Column(name = "PKYMORGID")
    private String pkYmOrgId;

    @Schema(name="外部信息部门负责人主键")
    @Column(name = "PRINCIPALCODE")
    private String principalCode;

    @Schema(name="外部信息分管领导主键")
    @Column(name = "CHARGELEADERCODE")
    private String chargeLeaderCode;
    
    @Schema(name="部门负责人主键")
    @Column(name = "PRINCIPALSTAFFID")
    private Integer principalStaffId;
    
    @Schema(name="用户直属主管用户主键")
    @Column(name = "CHARGELEADERSTAFFID")
    private Integer chargeLeaderStaffId;
    
    @Column(name ="BGIMAGE")
    @Schema(name="背景图")
    public String bgimage;
    
    @Column(name ="BGNAME")
    @Schema(name="背景图名称")
    public String bgname;
    
    
    @Column(name ="LOGOIMAGE")
    @Schema(name="logo")
    public String logoimage;
    
    @Column(name ="LOGONAME")
    @Schema(name="logo名称")
    public String logoname;
    
    
    @Column(name ="JDZTIMAGE")
    @Schema(name="经典主题标题")
    public String jdztimage;
    
    @Column(name ="JDZTNAME")
    @Schema(name="经典主题标题名称")
    public String jdztname;
    
    @Column(name ="CTZTIMAGE")
    @Schema(name="传统主题标题")
    public String ctztimage;
    
    @Column(name ="CTZTNAME")
    @Schema(name="传统主题标题名称")
    public String ctztname;
    
    
    @Column(name ="BANAME")
    @Schema(name="备案号")
    public String baname;
    
    @Schema(name="分管领导姓名")
    @Transient
    private String leaderName;
    
    @Schema(name="部门负责人姓名")
    @Transient
    private String principalName;
    
    
    @Schema(name="是否使用密级 0 不使用；1 使用")
    private Integer useSecrect;
    
    @Transient
    @Schema(name="是否选中")
    private Integer isChecked;
    
    @Transient
    @Schema(name="父级组织名称")
    private String fahterOrgName;
    
    @Transient
    private TblOrganizationResult tblOrganization;

    @JSONField(serialize = false )
    @Schema(name="子集组织信息")
    @Transient
    private List<TblOrganizationResult> children = new ArrayList<TblOrganizationResult>(0);
    
}
