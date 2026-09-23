package com.huabo.system.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "TBL_TRAININGSTAFF_ATT")
public class TblTrainingstaffAtt {

    @TableField("ATTID")
    private BigDecimal attId;//
    @TableField("STAFFID")
    private BigDecimal staffId;//
}
