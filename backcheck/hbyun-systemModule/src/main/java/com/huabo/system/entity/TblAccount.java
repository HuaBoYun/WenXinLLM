package com.huabo.system.entity;


import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_ACCOUNT")
@Schema(name="TblAccount")
public class TblAccount {

    private static final long serialVersionUID = 3942181650986783670L;
    @TableId(value="ACCID",type = IdType.INPUT)
    @Schema(name="科目编码")
    private String accId;
    @Transient
    @Schema(name="科目名称")
    private String accNameOne;
    @TableField("DC")
    @Schema(name="科目方向")
    private String dc;
    @TableField("HIGHACCID")
    @Schema(name="上级科目")
    private String highAccId;
    @TableField("TYPEID")
    private String typeId;
    @Transient
    @Schema(name="科目全名称")
    private String accNameTwo;
    @TableField("IGRADE")
    @Schema(name="科目级别")
    private Integer igrade;
    @TableField("SICASH")
    private Integer sicash;
    @TableField("ISBZKM")
    private Integer isbzkm;
    @TableField("ISDCACC")
    @Schema(name="是否是科目底层")
    private Integer isdcacc;
    @TableField("DES")
    @Schema(name="科目描述")
    private String des;
    @TableField("AYEAR")
    @Schema(name="年份")
    private Integer ayear;
    //HBFKCWZT
}
