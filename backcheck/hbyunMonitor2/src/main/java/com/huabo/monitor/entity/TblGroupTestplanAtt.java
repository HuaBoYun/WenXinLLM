package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

 
@Data
@TableName("TBL_GROUPTESTPLAN_ATT")
@Schema(name="TblGroupTestplanAtt对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblGroupTestplanAtt implements Serializable {

    private static final long serialVersionUID = 1L;
    // 联合主键
    private BigDecimal attid;
    private BigDecimal id;
 
}
