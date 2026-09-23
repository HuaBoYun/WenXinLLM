package com.huabo.legal.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Schema(name="LegalReviewAuditResult")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalReviewAuditResult {

	@Schema(name = "制度审核ID/经营事项审核ID (后端用于排序忽略)")
	private Integer id;

	@Schema(name = "制度审核ID")
	private Integer institutionAuditId;

	@Schema(name = "经营事项审核ID")
	private Integer operateMatterAuditId;

	@Schema(name = "名称")
	private String institutionName;

	@Schema(name = "类型")
	private Integer type;

	@Schema(name = "制度草案稿")
	private String institutionDraft;

	@Schema(name = "起草部门")
	private String draftDepartment;

	@Schema(name = "起草时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date draftTime;

	@Schema(name = "创建人(列表)")
	private String institutionCreator;

	@Schema(name = "创建时间（列表）")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date institutionCreatedTime;

	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;

	@Schema(name = "状态")
	private Integer state;

	@Schema(name = "创建人")
	private String creator;

	@Schema(name = "工作单位")
	private String workUnit;

	@Schema(name = "所属集团")
	private String belongGroup;

	@Schema(name = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@Schema(name = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	@Schema(name = "经营事项名称")
	private String operateMatterName;

	@Schema(name = "创建人(列表)")
	private String operateCreator;

	@Schema(name = "创建时间(列表)")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date operateCreatedTime;


}
