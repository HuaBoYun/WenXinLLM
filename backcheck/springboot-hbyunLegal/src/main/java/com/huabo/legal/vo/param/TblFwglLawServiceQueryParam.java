package com.huabo.legal.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Schema(name="TblFwglLawServiceQueryParam")
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglLawServiceQueryParam extends PageableParam implements Serializable {

	private static final long serialVersionUID = 1L;
	@Schema(name = "常年法律服务id/专项法律服务id")
	private Long lawServiceId;
	@NotNull(message = "lawServiceType,不能为空；法律服务类型1-常年 2-专项")
	@Schema(name = "法律服务类型1-常年 2-专项")
	private Integer lawServiceType;
	@Schema(name = "单位名称")
	private String unitName;
	@Schema(name = "聘请类型")
	private Integer employmentTermType;
	@Schema(name = "填报开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fillInBeginDate;
	@Schema(name = "填报结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fillInEndDate;
	@Schema(name = "状态")
	private Integer state;
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
	
	@Schema(name="单位Id",hidden=true)
	private String workUnitId;
	
	public TblFwglLawServiceQueryParam(Long lawServiceId, Integer lawServiceType) {
		this.lawServiceId = lawServiceId;
		this.lawServiceType = lawServiceType;
	}
}