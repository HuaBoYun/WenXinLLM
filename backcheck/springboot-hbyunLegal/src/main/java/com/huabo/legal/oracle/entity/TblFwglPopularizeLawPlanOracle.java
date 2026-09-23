package com.huabo.legal.oracle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 普法计划表
 */
@Schema(name="TblFwglPopularizeLawPlanOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_popularize_law_plan")
public class TblFwglPopularizeLawPlanOracle implements Serializable {

	@Id
	@Column(name = "POPULARIZELAWPLANID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "普法计划ID")
	private Long popularizeLawPlanId;

	@Column(name = "POPULARIZELAWPLANNAME")
	@Schema(name = "计划名称")
	private String popularizeLawPlanName;

	@Column(name = "PLANYEAR")
	@Schema(name = "年份")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	private Date planYear;

	@Column(name = "POPULARIZELAWCREATOR")
	@Schema(name = "创建人(列表)")
	private String popularizeLawCreator;

	@Column(name = "POPULARIZELAWCREATEDTIME")
	@Schema(name = "创建时间(列表)")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date popularizeLawCreatedTime;

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

	@Transient
	@Schema(name="所属集团名称",hidden=true)
	private String belongGroupName;

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

	@Column(name = "TOPIC")
	@Schema(name = "主题")
	private String topic;

	private static final long serialVersionUID = 1L;

	public static TblFwglPopularizeLawPlanOracle ofId(Long id) {
		TblFwglPopularizeLawPlanOracle tblFwglPopularizeLawPlanMySql = new TblFwglPopularizeLawPlanOracle();
		tblFwglPopularizeLawPlanMySql.setPopularizeLawPlanId(id);
		return tblFwglPopularizeLawPlanMySql;
	}
}
