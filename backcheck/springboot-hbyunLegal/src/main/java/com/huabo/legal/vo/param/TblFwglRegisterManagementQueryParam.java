package com.huabo.legal.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
@Schema(name="TblFwglRegisterManagementQueryParam")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglRegisterManagementQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Schema(name = "类别 1-商标 2-版权 3-专利")
	private Integer type;
	@Schema(name = "名称")
	private String registerName;
	@Schema(name = "注册开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerBeginDate;
	@Schema(name = "注册结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerEndDate;
	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
}