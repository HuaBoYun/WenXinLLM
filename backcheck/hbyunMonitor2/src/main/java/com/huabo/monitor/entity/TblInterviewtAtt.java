package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

 
@TableName("TBL_INTERVIEW_ATT")
@Schema(name="TblInterviewtAtt对象")
public class TblInterviewtAtt implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableField(value="ATTID")
    private BigDecimal attid;
    @TableField(value="ID")
    private BigDecimal ID;
    
}
