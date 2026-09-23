package com.huabo.finance.vr;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
@Schema(name="BdFinancedateRecord对象", description="财务数据采集记录表")
public class BdFinancedateRecordVr implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
      private String recordid;

      @Schema(name = "采集名称")
    private String recordname;

      @Schema(name = "采集SQL配置表")
    private String sqlid;

      @Schema(name = "数据采集方案信息表")
    private String planid;

      @Schema(name = "开始时间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startdate;

      @Schema(name = "结束时间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date enddate;

      @Schema(name = "采集类型，1-手动，2-自动")
    private Integer recordtype;

      @Schema(name = "是否完成 1-采集中，2-已完成")
    private Integer iscompleted;
      
      @Schema(name = "采集结果，0-失败 ， 1-成功 ")
    private Integer isresult;

      @Schema(name = "创建时间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;
      
      @Schema(name = "访问地址")
    private String recordip;

      @Schema(name = "采集记录")
    private String recordmemo;

      @Schema(name = "创建人")
    private BigDecimal creator;

      @Schema(name = "创建时间")
    private String creatname;

      @Schema(name = "所属部门")
    private BigDecimal linkdept;

      @Schema(name = "所属公司")
    private BigDecimal linkorg;

      @Schema(name = "方案名称")
      private String fname;

}
