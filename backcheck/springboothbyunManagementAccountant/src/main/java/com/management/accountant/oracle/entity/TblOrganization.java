package com.management.accountant.oracle.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_ORGANIZATION")
public class TblOrganization implements Serializable {

	@Id
	@Column(name = "ORGID")
	@GeneratedValue(generator = "JDBC")
	private Long orgid;

	@Column(name = "ORGNAME")
	@ApiModelProperty(value = "公司名称")
	private String orgname;

	@Column(name = "FATHERORGID")
	private Long fatherorgid;

	@Column(name = "ORGNUMBER")
	@ApiModelProperty(value = "公司编号")
	private String orgnumber;

	@Column(name = "ORGMENO")
	@ApiModelProperty(value = "公司简介")
	private String orgmeno;

	@Column(name = "MEMO")
	@ApiModelProperty(value = "备注")
	private String memo;

	@Column(name = "ICODE")
	private String icode;///行业架构ID（在哪个行业下创建行业知识库/行业缺陷库/行业问题库/行业数据库/行业指标库/行业规则库/行业模型库，该字段为哪个行业ID）

	@Column(name = "ORGTYPE")//是否是公司（普通部门为0，一级公司为1，二级公司为2，
	// 三级公司为3，行业架构为100，行业问题库为101，行业缺陷库为102，行业规则库为103，
	// 行业指标库为104，行业模型库为105，行业知识库为106，审计经验库为107，行业数据库为108）
	private Integer orgtype;

	@Column(name = "AUDITTYPE")//是否是主责部门/审计部（1是，0否）
	private Integer auditType;

	@Column(name = "STATUS")//状态（1弃用，0启用）
	private Integer status;

	@Column(name = "ISZY")//是否开启望远镜
	private String iszy;

	@Column(name = "HYZSKTYPE")//标识行业知识库的所属模块（风险管控为fxmanage，内部控制为nbkz，智能审计为znsj，智能监控为znjk）
	private String hyzsktype;

	@Column(name = "ORDERID")//排序编号，用于在显示组织架构排序
	private Integer orderid;

	@Column(name = "OUTSIDEID") //标识企业来源 为null是本系统，1：蜂信，以后可能为2,3...来表示其它来源   3:华博云注册公司   4:首冠注册用户  5:中财协注册用户  6：大成方略注册用户 7.用友用户
	private Integer outsideid;

	@Column(name = "OUTSIDEOPENDID")//外部同步企业来源Id
	private String outsideopendid;

	@ApiModelProperty(value = "是否使用自动编号 0 不使用；1 使用")
	@Column(name = "ISAUTONUMBER")
	private Integer isautonumber;

	@Column(name = "ORGCREATE")
	@ApiModelProperty("创建时间")
	private Date orgcreate;

	@Column(name = "ISINITIALIZATION")// 判断该组织有没有初始化 0初始化 ，
	private Integer isinitialization;

	@ApiModelProperty(value = "职务")
	@Column(name = "DUTIES")
	private String duties;

	@ApiModelProperty(value = "行业编号")
	@Column(name = "INDUSTRYID")
	private Integer industryid;

	@ApiModelProperty(value = "新增来源于微信 1为微信 0为pc")
	@Column(name = "BYWX")
	private String bywx;

	@Column(name = "DATASOURCE")
	@ApiModelProperty("数据来源")
	private String datasource;

	@Column(name = "HISTORYCODE")
	@ApiModelProperty("历史ID")
	private String historycode;

	@Column(name = "HISTORYDEPARTMENTID")
	@ApiModelProperty("历史部门ID")
	private String historydepartmentid;

	@ApiModelProperty(value = "发文代字")
	@Column(name = "WRITTENBYDEPT")
	private String writtenByDept;

	@Column(name = "PKYMORGID")
	@ApiModelProperty(value = "流程平台主键信息")
	private String pkYmOrgId;

	@Column(name = "PRINCIPALCODE")
	@ApiModelProperty(value = "外部信息部门负责人主键")
	private String principalCode;

	@Column(name = "CHARGELEADERCODE")
	@ApiModelProperty(value = "外部信息分管领导主键")
	private String chargeLeaderCode;

	@Column(name = "PRINCIPALSTAFFID")
	@ApiModelProperty(value = "部门负责人主键")
	private Integer principalStaffId;

	@Column(name = "CHARGELEADERSTAFFID")
	@ApiModelProperty(value = "用户直属主管用户主键")
	private Integer chargeLeaderStaffId;

	@Transient
	private List<TblOrganization> children;

	@Transient
	@ApiModelProperty("级别 0-是顶层")
	private Integer treeLevel;
}
