package com.huabo.fxgl.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.fxgl.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="缺陷管理列表查询入参")
public class TblFxgkBugVo extends BaseVo{
	
	@Schema(name = "缺陷id")
    private Integer bugid;
	
	@Schema(name = "缺陷编号")	
	private String bugnumber;
	
	@Schema(name = "缺陷级别")
    private String bugcriid;

    @Schema(name = "发现开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    
    @Schema(name = "发现结束时间 ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;
}

