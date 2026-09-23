package com.huabo.legal.oracle.entity;

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
 * 学习园地表
 */
@Table(name = "TBL_FWGL_LEARRNING")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblFwglLearrningOracle implements Serializable {

	@Id
	@Column(name = "LINGID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "主键ID")
	private Long lingid;

	@Column(name = "LINGCODE")
	@Schema(name = "编号")
	private String lingcode;

	@Column(name = "LINGDNAME")
	@Schema(name = "名称")
	private String lingdname;

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
 
