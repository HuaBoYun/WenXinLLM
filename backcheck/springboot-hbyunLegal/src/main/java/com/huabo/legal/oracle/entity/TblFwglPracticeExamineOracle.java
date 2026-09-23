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
 * 执业考核表
 */
@Schema(name="TblFwglPracticeExamineOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_practice_examine")
public class TblFwglPracticeExamineOracle implements Serializable {

	@Id
	@Column(name = "PRACTICEEXAMINEID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "执业考核ID")
	private Long practiceExamineId;

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
	@Schema(name = "所属单位(列表)")
	private String practiceExamineBelongGroup;

	@Transient
	@Schema(name="所属单位名称(列表)",hidden=true)
	private String practiceExamineBelongGroupName;

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
	@Schema(name="状态",hidden=true)//0 '未审批' 1'审批中' 2'已退回'3 '已通过'4'已终止' 5 '已跟踪'6'已完成'
	private Integer state;

	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;

	@Transient
	@Schema(name="创建人名称",hidden=true)
	private String creatorName;

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

	public static TblFwglPracticeExamineOracle ofId(Long id) {
		TblFwglPracticeExamineOracle tblFwglPracticeExamineMySql = new TblFwglPracticeExamineOracle();
		tblFwglPracticeExamineMySql.setPracticeExamineId(id);
		return tblFwglPracticeExamineMySql;
	}
}
