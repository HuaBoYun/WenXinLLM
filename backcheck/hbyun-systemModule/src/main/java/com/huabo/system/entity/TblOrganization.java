package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

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
@Schema(name="TblOrganization对象", description="")
@Table(name = "TBL_ORGANIZATION")
public class TblOrganization implements Serializable {
    public static final Integer AUDITTYPE = 1;
	private static final long serialVersionUID = -5754340813001254466L;
    public static final String WPZJK = "wpzjk";

    @TableId(value="ORGID",type = IdType.INPUT)
    private BigDecimal orgid;

    @TableField("ORGNAME")
    @Schema(name="公司名称")
    private String orgname;

    @TableField("FATHERORGID")
    private BigDecimal fatherorgid;

    @TableField("ORGNUMBER")
    @Schema(name="公司编号")
    private String orgnumber;

    @TableField("ORGMENO")
    @Schema(name="公司简介")
    private String orgmeno;

    @TableField("MEMO")
    @Schema(name="备注")
    private String memo;

    @TableField("ICODE")
    private String icode;///行业架构ID（在哪个行业下创建行业知识库/行业缺陷库/行业问题库/行业数据库/行业指标库/行业规则库/行业模型库，该字段为哪个行业ID）

    @TableField("ORGTYPE")//是否是公司（普通部门为0，一级公司为1，二级公司为2，
    // 三级公司为3，行业架构为100，行业问题库为101，行业缺陷库为102，行业规则库为103，
    // 行业指标库为104，行业模型库为105，行业知识库为106，审计经验库为107，行业数据库为108）
    private Integer orgtype;

    @TableField("AUDITTYPE")//是否是主责部门/审计部（1是，0否）
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

    @Schema(name="是否使用自动编号 0 不使用；1 使用")
    @TableField("ISAUTONUMBER")
    private Integer isautonumber;

    @Schema(name="是否使用密级 0 不使用；1 使用")
    @TableField("USESECRECT")
    private Integer useSecrect;
    
    @Schema(name="父级编号判断同公司组织下的重复信息")
    @TableField("UNIQUENUMBER")
    private String uniqueNumber;
    
    
    @TableField("ORGCREATE")
    @Schema(name="创建时间")
    private Date orgcreate;

    @TableField("ISINITIALIZATION")// 判断该组织有没有初始化 0初始化 ，
    private Integer isinitialization;

    @Schema(name="职务")
    @TableField("DUTIES")
    private String duties;

    @Schema(name="行业编号")
    @TableField("INDUSTRYID")
    private Integer industryid;

    @Schema(name="新增来源于微信 1为微信 0为pc")
    @TableField("BYWX")
    private String bywx;

    @TableField("DATASOURCE")
    @Schema(name="数据来源")
    private String datasource;

    @TableField("HISTORYCODE")
    @Schema(name="历史ID")
    private String historycode;

    @TableField("HISTORYDEPARTMENTID")
    @Schema(name="历史部门ID")
    private String historydepartmentid;
    
    @Schema(name="发文代字")
    @TableField("WRITTENBYDEPT")
    private String writtenByDept;

    @TableField("PKYMORGID")
    @Schema(name="流程平台主键信息")
    private String pkYmOrgId;

    @TableField("PRINCIPALCODE")
    @Schema(name="外部信息部门负责人主键")
    private String principalCode;

    @TableField("CHARGELEADERCODE")
    @Schema(name="外部信息分管领导主键")
    private String chargeLeaderCode;
    
    @TableField("PRINCIPALSTAFFID")
    @Schema(name="部门负责人主键")
    private BigDecimal principalStaffId;
    
    @TableField("CHARGELEADERSTAFFID")
    @Schema(name="用户直属主管用户主键")
    private BigDecimal chargeLeaderStaffId;
    
    @TableField("ORGANIZATIONTREES")
    @Schema(name="父级组织")
    public String organizationTrees;
    
    
    @TableField("JTORGID")
    @Schema(name="关联集团ID")
    private BigDecimal jtorgid;
    
    @TableField("JTORGNAME")
    @Schema(name="关联集团名称")
    private String jtorgname;
    
    @TableField("BGIMAGE")
    @Schema(name="背景图")
    private String bgimage;
    
    @TableField("BGNAME")
    @Schema(name="背景图名称")
    private String bgname;
    
    
    @TableField("LOGOIMAGE")
    @Schema(name="logo")
    private String logoimage;
    
    @TableField("LOGONAME")
    @Schema(name="logo名称")
    private String logoname;
    
    
    @TableField("JDZTIMAGE")
    @Schema(name="经典主题标题")
    private String jdztimage;
    
    @TableField("JDZTNAME")
    @Schema(name="经典主题标题名称")
    private String jdztname;
    
    @TableField("CTZTIMAGE")
    @Schema(name="传统主题标题")
    private String ctztimage;
    
    @TableField("CTZTNAME")
    @Schema(name="传统主题标题名称")
    private String ctztname;
    
    @TableField("BANAME")
    @Schema(name="备案号")
    private String baname;
    
    
    @Schema(name="分管领导姓名")
    @TableField(exist = false)
    private String leaderName;
    
    @TableField(exist = false)
    @Schema(name="部门负责人姓名")
    private String principalName;
    
    @TableField(exist = false)
    @Schema(name="是否选中")
    private Integer isChecked;
    
    @TableField(exist = false)
    @Schema(name="父级公司名称")
    private String fahterOrgName;
    
    @TableField(exist = false)
    @Schema(name="组织架构树名称")
    private String orgTreeNames;
    
    @TableField(exist = false)
    private TblOrganization tblOrganization;
    
    @TableField(exist = false)
    @JSONField(serialize = false)
    private Set<TblManageRight> tblManageRights = new HashSet(0);

    @JSONField(serialize = false )
    @TableField(exist = false)
    private Set<TblOrganization> children = new HashSet();

    
    @TableField(exist = false)
    private List<TblOrganization> childrenList = new ArrayList<TblOrganization>(0);


	@Override
	public String toString() {
		return "TblOrganization [orgid=" + orgid + ", orgname=" + orgname + ", fatherorgid=" + fatherorgid
				+ ", orgnumber=" + orgnumber + ", status=" + status + ", uniqueNumber=" + uniqueNumber + ", datasource="
				+ datasource + ", pkYmOrgId=" + pkYmOrgId + "]";
	}

}
