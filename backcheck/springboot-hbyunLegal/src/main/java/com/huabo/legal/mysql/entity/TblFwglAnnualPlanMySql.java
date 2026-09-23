package com.huabo.legal.mysql.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 年度计划表
 */
@Schema(name="TblFwglAnnualPlanMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_annual_plan")
public class TblFwglAnnualPlanMySql implements Serializable {

	@Id
	@Column(name = "ANNUALPLANID")
	@Schema(name = "年度计划ID")
	@GeneratedValue(generator = "JDBC")
	private Integer annualPlanId;

	@Column(name = "ANNUALPLANNAME")
	@Schema(name = "计划名称")
	private String annualPlanName;

	@Column(name = "ANNUALPLANCREATOR")
	@Schema(name = "创建人")
	private String annualPlanCreator;

	@Column(name = "ANNUALPLANCREATEDTIME")
	@Schema(name = "创建时间")
	private String annualPlanCreatedTime;

	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Column(name = "STATE")
	@Schema(name="状态",hidden=true)
	private Integer state;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;

	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

	@Column(name = "CREATEDTIME")
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@Column(name = "UPDATEDTIME")
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	private static final long serialVersionUID = 1L;

	public static TblFwglAnnualPlanMySql ofId(Integer id) {
		TblFwglAnnualPlanMySql tblFwglAnnualPlanMySql = new TblFwglAnnualPlanMySql();
		tblFwglAnnualPlanMySql.setAnnualPlanId(id);
		return tblFwglAnnualPlanMySql;
	}
}
