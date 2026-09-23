package com.huabo.compliance.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.entity
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:18:12
 */
@TableName("TBL_HGGL_ARET")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
@Table(name = "TBL_HGGL_ARET")
public class TblhgglAret {

	@TableField(value = "ID")
	@Schema(name = "主键")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	private Integer id;

	@TableField(value = "NUMBERNO")
	@Schema(name = "序号")
	private Integer numberno;

	@TableField(value = "ISUUE")
	@Schema(name = "问题")
	private String isuue;

	@TableField(value = "TYPE")
	@Schema(name = "类型")
	private String type;

	@TableField(value = "BUSINESS")
	@Schema(name = "业务领域")
	private String business;

	@TableField(value = "RECTIFICATION")
	@Schema(name = "是否完成整改（是/否）")
	private Integer rectification;

	@TableField(value = "RECTIFICATIONTIME")
	@Schema(name = "整改完成时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date rectificationtime;

	@Column(name = "FILEIDS")
	@Schema(name = "上传文件ids 多个逗号隔开")
	private String fileids;
}
