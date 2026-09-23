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
 * 学法考试表
 */
@Schema(name="TblFwglXfExamMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_xf_exam")
public class TblFwglXfExam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "EXAMID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "学法考试ID")
	private Long examId;
	@Column(name = "EXAMNAME")
	@Schema(name = "名称")
	private String examName;
	@Column(name = "TYPE")
	@Schema(name = "类型")
	private String type;
	@Column(name = "SUBJECT")
	@Schema(name = "学法主题")
	private String subject;
	@Column(name = "PARTICIPANT")
	@Schema(name = "参加人员")
	private String participant;
	@Column(name = "CONTENT")
	@Schema(name = "学法内容")
	private String content;
	@Column(name = "EXAMCREATOR")
	@Schema(name = "创建人")
	private String examCreator;
	@Column(name = "EXAMCREATEDTIME")
	@Schema(name = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date examCreatedTime;
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

	public static TblFwglXfExam ofId(Long id) {
		TblFwglXfExam tblFwglXfExamMySql = new TblFwglXfExam();
		tblFwglXfExamMySql.setExamId(id);
		return tblFwglXfExamMySql;
	}
}
