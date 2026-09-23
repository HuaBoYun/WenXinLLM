package com.huabo.monitor.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_FLOW_BUSSINESS")
@Schema(name="TblFlowBussiness")
public class TblFlowBussinessMySql implements Serializable {

    @TableId("BUSSINESSID")
    @Id
    private BigDecimal bussinessid;
    @TableField("BUSSINESSNUMBER")
    private String bussinessnumber;
    @TableField("BUSSINESSNAME")
    private String bussinessname;//业务名称
    @TableField("BUSSINESSDES")
    private String bussinessdes;//业务描述
    @TableField("FLOWID")
    private Long flowid;
}
