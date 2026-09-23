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
 * 专项法律服务表-考核表
 */
@Schema(name="TblFwglSpecialLawServiceExamine")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_special_law_service_examine")
public class TblFwglSpecialLawServiceExamine implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Schema(name = "考核ID")
	@GeneratedValue(generator = "JDBC")
	private Long examineId;

	@Schema(name = "团队名称")
	private String teamName;

	@Schema(name = "有无合作")
	private String isCollaboration;

	@Schema(name = "委托主体")
	private String entrustSubject;

	@Schema(name = "服务项目")
	private String serviceProject;

	@Schema(name = "专业能力（60分）")
	private String professionalAbilityGrade;

	@Schema(name = "响应效率（30分）")
	private String reactionEfficiencyGrade;

	@Schema(name = "增值服务（10分）")
	private String appreciationServiceGrade;

	@Schema(name = "备注")
	private String remark;

	@Schema(name="状态",hidden=true)
	private Integer state;

	@Schema(name="创建人",hidden=true)
	private String creator;

	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	@Schema(name = "合计")
	private String total;

	@Schema(name = "平均分")
	private String average;


	public static TblFwglSpecialLawServiceExamine ofId(Long id) {
		TblFwglSpecialLawServiceExamine tblFwglSpecialLawServiceExamineMySql = new TblFwglSpecialLawServiceExamine();
		tblFwglSpecialLawServiceExamineMySql.setExamineId(id);
		return tblFwglSpecialLawServiceExamineMySql;
	}
}
