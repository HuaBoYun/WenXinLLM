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

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 其他文件报送
 */
@Schema(name="TblFwglOtherFileMessage")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_other_file_message")
public class TblFwglOtherFileMessage implements Serializable {

	private static final long serialVersionUID = 1L;
//	@ExcelField(title = "其他文件报送ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "其他文件报送ID")
	private Long otherFileMessageId;
	@ExcelField(title = "文件名称", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "文件名称")
	private String fileName;
	@ExcelField(title = "创建人", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "创建人")
	private String otherFileMessageCreator;
//	@ExcelField(title = "创建时间（列表）", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "创建时间（列表）")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date otherFileMessageCreatedTime;
//	@ExcelField(title = "富文本框", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "富文本框")
	private String content;
	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileIds;
	@ExcelField(title = "状态", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SHSTATE_TYPE)
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
	@ExcelField(title = "创建时间", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
//	@ExcelField(title = "更新时间", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	public static TblFwglOtherFileMessage ofId(Long id) {
		TblFwglOtherFileMessage tblFwglOtherFileMessageMySql = new TblFwglOtherFileMessage();
		tblFwglOtherFileMessageMySql.setOtherFileMessageId(id);
		return tblFwglOtherFileMessageMySql;
	}
}
