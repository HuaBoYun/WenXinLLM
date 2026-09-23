package com.huabo.compliance.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_FLOWDES")
@Schema(name="TblFlowdes")
public class TblFlowdesMySql {

    @TableId("FLOWDESID")
    private BigDecimal flowdesid;
    @Transient
    private TblFlowMySql tblFlowMySql;
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
