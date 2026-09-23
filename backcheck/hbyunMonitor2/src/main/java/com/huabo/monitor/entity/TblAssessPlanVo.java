package com.huabo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.monitor.vo.param.TblBugCriterionEntityParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评价计划
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblAssessPlanVo{


    @Schema(name="评价计划ID")
    @TableId(type= IdType.INPUT)
    private BigDecimal assid;

    @Schema(name="评价计划编号")
    private String assessid;
    
    @Schema(name="评价计划名称")
    private String assessname;

    @Schema(name="开始时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime startdate;

    @Schema(name="结束是时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime enddate;

    
    @Schema(name="开始时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime startdates;

    @Schema(name="结束是时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime enddates;
    @Schema(name="备注")
    private String memo;

    @Schema(name="状态(1创建2启动)")
    private String assstatus;

    @Schema(name="发起日期")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime assstartday;

    @Schema(name="评价对象IDs")
    private String assobjids;
    
    @Schema(name="评价对象名称s")
    private String assobjnames;

    //TBL_STAFF 外键
    @Schema(name="评价负责人")
    private BigDecimal leaderid;

    @Schema(name="负责人名字")
    private String realname;

    @Schema(name="负责人登录名")
    private String username;

    @Schema(name="审批状态")
    private Integer status;
    
    @Schema(name="创建人")
    private BigDecimal createstaffid;
    
    @Schema(name="创建时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime createtime;

    @Schema(name="是否是审计部门")
    private boolean isAudit;
    
    @Schema(name="判断用户权限")
    private Integer   authorityType;
     
    
}
