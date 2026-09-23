package com.huabo.legal.vo.result;

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
 * 常年法律服务-评分表
 */
@Schema(name="TblFwglPerennialLawServiceGrade")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_perennial_law_service_grade")
public class TblFwglPerennialLawServiceGrade implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "GRADEID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "评分ID")
	private Long gradeId;
	@Column(name = "BUSINESSPREMISESNAME")
	@Schema(name = "事务所名称")
	private String businessPremisesName;
	@Column(name = "SERVICEQUALITYGRADE")
	@Schema(name = "服务质量评分")
	private String serviceQualityGrade;
	@Column(name = "COMMUNICATIONANDCOLLABORATIONGRADE")
	@Schema(name = "沟通合作评分")
	private String communicationAndCollaborationGrade;
	@Column(name = "APPRECIATIONSERVICEGRADE")
	@Schema(name = "增值服务评分")
	private String appreciationServiceGrade;
	@Column(name = "EXAMINEGRADE")
	@Schema(name = "考核结果")
	private String examineGrade;
	@Column(name = "PROPOSAL")
	@Schema(name = "其他意见或建议")
	private String proposal;
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

	public static TblFwglPerennialLawServiceGrade ofId(Long id) {
		TblFwglPerennialLawServiceGrade tblFwglPerennialLawServiceGradeMySql = new TblFwglPerennialLawServiceGrade();
		tblFwglPerennialLawServiceGradeMySql.setGradeId(id);
		return tblFwglPerennialLawServiceGradeMySql;
	}
}
