package com.huabo.legal.oracle.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 常年法律服务-评价表
 */
@Schema(name="TblFwglLawServiceEvaluate")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_FWGL_LAW_SERVICE_EVALUATE")
public class TblFwglLawServiceEvaluateOracle implements Serializable {
	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long id;

	/**
	 * 事务所名称
	 */
	@Column(name = "ORGANIZATIONNAME")
	@Schema(name = "事务所名称")
	private String organizationName;

	/**
	 * 委托期限
	 */
	@Column(name = "ENTRUSTTIMELIMIT")
	@Schema(name = "委托期限")
	private String entrustTimeLimit;

	/**
	 * json前端展示需要
	 */
	@Column(name = "DETAILLISTJSON")
	@Schema(name = "json前端展示需要")
	private String detailListJson;

	/**
	 * 总分
	 */
	@Column(name = "TOTALSCORE")
	@Schema(name = "总分")
	private String totalScore;

	/**
	 * 考核结果（满意或不满意）
	 */
	@Column(name = "EXAMINEGRADE")
	@Schema(name = "考核结果（满意或不满意）")
	private String examineGrade;

	/**
	 * 其他意见或建议
	 */
	@Column(name = "OPINIONS")
	@Schema(name = "其他意见或建议")
	private String opinions;

	/**
	 * 聘用单位
	 */
	@Column(name = "HIREUNIT")
	@Schema(name = "聘用单位ID")
	private Long hireUnit;

	@Transient
	@Schema(name = "聘用单位名称")
	private String hireUnitName;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	/**
	 * 创建人ID
	 */
	@Column(name = "CREATOR")
	@Schema(name="创建人ID",hidden=true)
	private String creator;

	/**
	 * 工作单位ID
	 */
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位ID",hidden=true)
	private String workUnit;

	/**
	 * 所属集团ID
	 */
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团ID",hidden=true)
	private String belongGroup;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATEDTIME")
	@Schema(name="创建时间",hidden=true)
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@Column(name = "UPDATEDTIME")
	@Schema(name="更新时间",hidden=true)
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblFwglLawServiceEvaluateOracle ofId(Long id) {
		TblFwglLawServiceEvaluateOracle tblFwglLawServiceEvaluateOracle = new TblFwglLawServiceEvaluateOracle();
		tblFwglLawServiceEvaluateOracle.setId(id);
		return tblFwglLawServiceEvaluateOracle;
	}
}