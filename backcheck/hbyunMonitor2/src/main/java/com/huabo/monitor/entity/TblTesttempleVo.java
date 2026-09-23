package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author：yhr
 * @date:2022-09-08 15:12
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblTesttempleVo implements Serializable {



    private static final long serialVersionUID = 1L;

    private BigDecimal testtemid;

    private String templename;//模板名称

    private String templenumber;//模板编号

    private String memo;

   	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
   	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createtime;//创建时间

    private BigDecimal templestatus;

    private String templedesc;//模板说明

    private String tblcomany;//公司名称

    private BigDecimal staffid;//创建者id

    private BigDecimal secrectLevelId;
    
    private String staffScopeIds;
    
    private String staffScopeNames;


    private String source;//来源：自建、上级公司名称

    private TblStaff  chuangjianren;
}
