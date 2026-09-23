package com.huabo.central.enterprises.audit.oracle.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 附件信息
 * @TableName TBL_ATTACHMENT
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_ATTACHMENT")
public class TblAttachment {

	@Id
	@Column(name = "ATTID")
	@Schema(name = "主键ID")
	@GeneratedValue(generator = "JDBC")
	private Long attid;

	@Schema(name = "文件名称")
	@Column(name = "ATTNAME")
	private String attname;

	@Schema(name = "文件路径")
	@Column(name = "ATTPATH")
	private String attpath;

	@Schema(name = "文件大小")
	@Column(name = "ATTSIZE")
	private double attsize;

	@Schema(name = "文件备注")
	@Column(name = "MEMO")
	private String memo;

	@Schema(name = "上传时间")
	@Column(name = "UPLOADTIME")
	private Date uploadtime;

	@Schema(name = "上传人")
	@Column(name = "UPLOADER")
	private String uploader;

	@Schema(name = "是否是python爬取文件 0是")
	@Column(name = "ISPYTHONFLAG")
	private String ispythonflag;

	@Column(name = "STAFFIDS")
	@Schema(name = "审计项目资料下发给组员字段")
	private String staffids;

	@Transient
	private String filename;
	@Transient
	private String contentText;
}