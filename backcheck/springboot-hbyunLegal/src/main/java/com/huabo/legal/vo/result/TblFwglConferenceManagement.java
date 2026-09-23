package com.huabo.legal.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.excel.DictMapUtil;
import com.huabo.legal.util.excel.annotation.ExcelField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 会议管理
 */
@Schema(name="TblFwglConferenceManagement")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglConferenceManagement implements Serializable {

	private static final long serialVersionUID = 1L;
	@ExcelField(title = "会议ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "会议ID")
	private Long conferenceId;
	@ExcelField(title = "会议名称", sort = 1, column = 1, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "会议名称")
	private String conferenceName;

	@ExcelField(title = "会议主持人", sort = 2, column = 2, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "会议主持人ID")
	@Pattern(regexp = "^[0-9]*$", message = "compere需要输入数字类型")
	private String compere;

	@Schema(name = "会议参与者ID 多个参与者用逗号隔开")
	private String participants;

	@ExcelField(title = "会议时间", sort = 3, column = 3, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "会议时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date conferenceTime;
//	@ExcelField(title = "创建人(列表)", sort = 4, column = 4, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "创建人(列表)")
	private String conferenceCreator;
//	@ExcelField(title = "创建时间(列表)", sort = 5, column = 5, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "创建时间(列表)")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date conferenceCreatedTime;
//	@ExcelField(title = "会议内容", sort = 6, column = 6, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "会议内容")
	private String content;
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;
//	@ExcelField(title = "状态", sort = 7, column = 7, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.STATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
	@ExcelField(title = "创建时间", sort = 8, column = 8, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
//	@ExcelField(title = "更新时间", sort = 9, column = 9, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;
}