package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件存储表
 */
@Schema(name = "TblFwglFileOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tbl_system_file")
public class TblSystemFileOracle implements Serializable {

	@TableId(value = "FILEID",type = IdType.INPUT)
	@Schema(name="主键Id 自增")
	private BigDecimal fileId;

	@TableField(value = "FILENAME")
	@Schema(name="附件名称")
	private String fileName;

	@TableField(value = "FILEPATH")
	@Schema(name="附件路径")
	private String filePath;

	@TableField(value = "FILESIZE")
	@Schema(name="附件大小")
	private Double fileSize;

	@TableField(value = "REMARK")
	@Schema(name="备注")
	private String remark;

	@TableField(value = "UPLOADTIME")
	@Schema(name="上传时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date uploadTime;

	@TableField(value = "UPLOADER")
	@Schema(name="上传人")
	private String uploader;

	@TableField(value = "ISPYTHONFLAG")
	@Schema(name="是否是python爬取文件 0是")
	private String isPythonFlag;

	private static final long serialVersionUID = 1L;
}
