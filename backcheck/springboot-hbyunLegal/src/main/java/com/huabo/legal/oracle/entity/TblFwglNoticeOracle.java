package com.huabo.legal.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 通知公告
 */
@TableName("TBL_FWGL_NOTICE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblFwglNoticeOracle implements Serializable {

	@Id
	@Column(name = "NOTICEID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long noticeid;

	@Column(name = "NOTICECODE")
	@Schema(name = "编号")
	private String noticecode;

	@Column(name = "NOTICENAME")
	@Schema(name = "名称")
	private String noticename;

	@Column(name = "CREATENAME")
	@Schema(name = "创建人名称")
	private String createname;

	@Column(name = "CREATESTAFF")
	@Schema(name = "创建人")
	private Long createstaff;
 

	@Column(name = "ORGID")
	@Schema(name="所属公司",hidden=true)
	private Long orgid;

	@Column(name = "CREATETIME")
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createtime;

 

	private static final long serialVersionUID = 1L;
}
 
