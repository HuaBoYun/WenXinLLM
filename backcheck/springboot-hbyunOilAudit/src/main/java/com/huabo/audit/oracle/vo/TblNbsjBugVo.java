package com.huabo.audit.oracle.vo;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Date;

@Data
@Schema(name="缺陷管理列表查询入参")
public class TblNbsjBugVo extends BaseVo{
	
	@Schema(name = "缺陷id")
    private Integer bugid;
	
	@Schema(name = "缺陷编号")	
	private String bugnumber;
	
	@Schema(name = "缺陷级别")
    private String bugcriid;

    @Schema(name = "发现开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;
    
    @Schema(name = "发现结束时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;

    public Date getStartSqlDate(){
        return DateUtil.parseDate(this.getStartDate()).toSqlDate();
    }

    public Date getEndSqlDate(){
        return DateUtil.parseDate(this.getEndDate()).toSqlDate();
    }
}

