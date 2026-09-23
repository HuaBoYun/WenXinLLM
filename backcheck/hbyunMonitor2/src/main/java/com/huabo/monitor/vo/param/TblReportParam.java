package com.huabo.monitor.vo.param;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-17
 */
@Data
public class TblReportParam  implements Serializable {
      
    private BigDecimal reportid;//主键（自增）

	@Schema(name = "报告名称")
    private String reportname;//报告名称

	@Schema(name = "报告时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reporttime;//

	@Schema(name = "报告类型")
    private String reporttype;//

 	@Schema(name = "报告方式")
    private String reportmode;//定期报告

   	@Schema(name = "报告人")
    private String reporter;//报告人

   	@Schema(name = "报告部门")
    private String reportdepartment;//报告部门

	@Schema
    private String startDate;

	@Schema
    private String endDate;
	
   @Schema(name = "类型")
   private String type;//类型，如fx,nk,nbsj
   
  	@Schema(name = "报告部门")
   private BigDecimal orgid;

   	@Schema(name = "报告人")
    private BigDecimal createstaffid;
   	
   	//
   	@Schema
    private String startDateYear;

	@Schema
    private String endDateYear;
	
}
