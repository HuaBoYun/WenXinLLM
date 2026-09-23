package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

 
@TableName("TBL_REPORT_BUG")
@Schema(name="TblReportBug对象", description="内控报告关联评价缺陷")
@KeySequence(value="HIBERNATE_SEQUENCE")
@Data
public class TblReportBug implements Serializable {
   
    @TableId(type= IdType.INPUT)
    private BigDecimal id;//主键（自增）
 
    
    @TableField("REPORTID")
    private BigDecimal reportid;//内控报告id
    
    @TableField("BUGID")
    private BigDecimal bugid;//评价缺陷id
}
