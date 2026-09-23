package com.huabo.system.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 系统业务单据下发通知表
 * </p>
 *
 * @author LHP
 * @since 2023-11-24
 */
@Data
@Schema(name="TblSystemDistribution返回对象", description="系统业务单据下发通知表返回对象")
public class TblSystemDistributionVo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name="下发事项主键")
      private String distributionId;

    @Schema(name="下发事项标题")
    private String distributionTitle;

      @Schema(name="接收人")
    private BigDecimal reciver;

      @Schema(name="创建时间，下发时间")
      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

      @Schema(name="接受确认时间")
      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date reciveDate;

      @Schema(name="业务单据类型  ,来源 TBl_system_sheettable 中的 tableId")
    private String distributionType;

      @Schema(name="业务单据主键")
    private String formId;

      @Schema(name="所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx 整改追责-zgzz 业务管控财经实训等后续新增的一级权限 为该权限的Id")
    private String moduleType;

      @Schema(name="创建人 下发人")
    private BigDecimal createStaff;

      @Schema(name="是否接受 0-否  1-是")
    private Integer isread;

      @Schema(name="路由")
    private String pageUrl;
      
      @Schema(name="创建人姓名")
      private String createStaffName;

      @Schema(name="接收人姓名")
      private String reciverName;
      
      @Schema(name="序号")
      private Integer rowNo;

      @Schema(name="筛选条件，下发时间开始时间")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createStartDate;
      
      @Schema(name="筛选条件，下发时间结束时间")
      @JSONField(format = "yyyy-MM-dd")
      @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createEndDate;
}
