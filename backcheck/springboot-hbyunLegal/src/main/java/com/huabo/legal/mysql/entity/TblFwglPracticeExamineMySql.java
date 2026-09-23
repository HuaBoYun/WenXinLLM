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
 * 执业考核表
 */
@Schema(name="TblFwglPracticeExamineMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_practice_examine")
public class TblFwglPracticeExamineMySql implements Serializable {

	@Id
	@Column(name = "PRACTICEEXAMINEID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "执业考核ID")
	private Integer practiceExamineId;

	@Column(name = "PRACTICEEXAMINENAME")
	@Schema(name = "姓名")
	private String practiceExamineName;

	@Column(name = "SEX")
	@Schema(name = "性别")
	private Integer sex;

	@Column(name = "PRACTICEAGELIMIT")
	@Schema(name = "已执业年限")
	private String practiceAgeLimit;

	@Column(name = "EXAMINEGRADE")
	@Schema(name = "考核结果")
	private Integer examineGrade;

	@Column(name = "PRACTICEEXAMINEBELONGGROUP")
	@Schema(name = "所属集团(列表)")
	private String practiceExamineBelongGroup;

	@Column(name = "PRACTICEEXAMINECREATEDTIME")
	@Schema(name = "创建时间(列表)")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date practiceExamineCreatedTime;

	@Column(name = "PRACTICEEXAMINECREATOR")
	@Schema(name = "创建人(列表)")
	private String practiceExamineCreator;

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

	public static TblFwglPracticeExamineMySql ofId(Integer id) {
		TblFwglPracticeExamineMySql tblFwglPracticeExamineMySql = new TblFwglPracticeExamineMySql();
		tblFwglPracticeExamineMySql.setPracticeExamineId(id);
		return tblFwglPracticeExamineMySql;
	}
}
