package com.huabo.system.entity;


import java.math.BigDecimal;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_FLOWDES")
@Schema(name="TblFlowdes")
public class TblFlowdes {

    @TableId(value="FLOWDESID",type = IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal flowdesid;
    @Transient
    private TblFlow tblFlow;
    @TableField("PROCEDURENUMBER")
    private String procedurenumber;
    @TableField("PROCEDURENAME")
    private String procedurename;
    @TableField("DEPARTINCHARGE")
    private String departincharge;
    @TableField("PROCEDUREDES")
    private String proceduredes;
    @TableField("MEMO")
    private String memo;
    @TableField("ATTACHMENT")
    private String attachment;
    @TableField("POSITION")
    private Integer position;
}
