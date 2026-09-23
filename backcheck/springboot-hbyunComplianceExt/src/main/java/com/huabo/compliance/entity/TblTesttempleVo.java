package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author：yhr
 * @date:2022-09-08 15:12
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("TBL_COM_EXT_TESTPLAN")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblTesttempleVo implements Serializable {



    private static final long serialVersionUID = 1L;

    private BigDecimal testtemid;

    private String templename;//模板名称

    private String templenumber;//模板编号

    private String memo;

    private LocalDateTime createdatetime;//创建时间

    private BigDecimal templestatus;

    private String templedesc;//模板说明

    private String tblcomany;//公司名称

    private BigDecimal staffid;//创建者id



    private String source;//来源：自建、上级公司名称

    private TblStaff  chuangjianren;
}
