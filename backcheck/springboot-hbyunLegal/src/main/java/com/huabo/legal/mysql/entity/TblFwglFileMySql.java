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
 * 法务管理-文件存储表
 */
@Schema(name="TblFwglFileMySql")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_file")
public class TblFwglFileMySql implements Serializable {

	@Id
	@Column(name = "FILEID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键Id 自增")
	private Integer fileId;

	@Column(name = "FILENAME")
	@Schema(name = "附件名称")
	private String fileName;

	@Column(name = "FILEPATH")
	@Schema(name = "附件路径")
	private String filePath;

	@Column(name = "FILESIZE")
	@Schema(name = "附件大小")
	private Double fileSize;

	@Column(name = "REMARK")
	@Schema(name = "备注")
	private String remark;

	@Column(name = "UPLOADTIME")
	@Schema(name = "上传时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date uploadTime;

	@Column(name = "UPLOADER")
	@Schema(name = "上传人")
	private String uploader;

	@Column(name = "ISPYTHONFLAG")
	@Schema(name = "是否是python爬取文件 0是")
	private String isPythonFlag;

	private static final long serialVersionUID = 1L;
}
