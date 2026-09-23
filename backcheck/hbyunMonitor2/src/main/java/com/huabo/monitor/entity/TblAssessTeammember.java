package com.huabo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Data
@TableName("TBL_ASSESS_TEAMMEMBER")
@Schema(name="TBL_ASSESS_TEAMMEMBER对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblAssessTeammember implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type= IdType.INPUT)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField("ASSID")
    @Schema(name="评价立项id")
    private BigDecimal assid;

    @TableField("STAFFID")
    @Schema(name="关联人员")
    private String staffid;
 
     
    
}
