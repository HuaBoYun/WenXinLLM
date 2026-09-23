package com.management.accountant.oracle.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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
	@ApiModelProperty(value = "主键ID")
	@GeneratedValue(generator = "JDBC")
	private Long attid;

	@ApiModelProperty(value = "文件名称")
	@Column(name = "ATTNAME")
	private String attname;

	@ApiModelProperty(value = "文件路径")
	@Column(name = "ATTPATH")
	private String attpath;

	@ApiModelProperty(value = "文件大小")
	@Column(name = "ATTSIZE")
	private double attsize;

	@ApiModelProperty(value = "文件备注")
	@Column(name = "MEMO")
	private String memo;

	@ApiModelProperty(value = "上传时间")
	@Column(name = "UPLOADTIME")
	private Date uploadtime;

	@ApiModelProperty(value = "上传人")
	@Column(name = "UPLOADER")
	private String uploader;

	@ApiModelProperty(value = "是否是python爬取文件 0是")
	@Column(name = "ISPYTHONFLAG")
	private String ispythonflag;

	@Column(name = "STAFFIDS")
	@ApiModelProperty(value = "审计项目资料下发给组员字段")
	private String staffids;

	@Transient
	private String filename;
	@Transient
	private String contentText;
}