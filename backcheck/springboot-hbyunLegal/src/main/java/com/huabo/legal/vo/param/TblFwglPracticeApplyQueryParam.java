package com.huabo.legal.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@Schema(name="TblFwglPracticeApplyQueryParam")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglPracticeApplyQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Schema(name = "姓名")
	private String practiceApplyName;
	
	@Schema(name = "人员id")
	private String staffId;

	@Schema(name="1-有职业资格证 0-无职业资格证")
	private Integer isCertificationNumber;

	@Schema(name="1-有公司职业资格证 0-无公司职业资格证")
	private Integer isCertificateNumber;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "开始年", required = true)
	private Date startTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "结束年", required = true)
	private Date endTime;

	@Schema(name = "状态")
	private String state;
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
	
	@Schema(name = "是否执业申请")
	private String iszysq;
	
}