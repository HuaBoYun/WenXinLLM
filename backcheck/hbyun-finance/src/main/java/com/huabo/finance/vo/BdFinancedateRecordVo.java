package com.huabo.finance.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 财务数据采集记录表
 * </p>
 *
 * @author L
 * @since 2025-03-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
@Schema(name="BdFinancedateRecordVo对象", description="财务数据采集记录表")
public class BdFinancedateRecordVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "采集名称")
      private String recordname;

      @Schema(name = "数据采集方案名称")
      private String fname;

      @Schema(name = "开始时间-起始")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      private Date minstartdate;
        
      @Schema(name = "开始时间-结束")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      private Date maxstartdate;
        
      @Schema(name = "结束时间-起始")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      private Date minenddate;
      
      @Schema(name = "结束时间-结束")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      private Date maxenddate;

      @Schema(name = "采集类型，1-手动，2-自动")
      private Integer recordtype;

      @Schema(name = "是否完成 1-采集中，2-已完成")
      private Integer iscompleted;
        
      @Schema(name = "采集结果，0-失败 ， 1-成功 ")
      private Integer isresult;
      
      @Schema(name = "采集SQL配置表")
      private String sqlid;

}
