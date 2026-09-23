package com.huabo.monitor.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Transient;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_ACCOUNT")
@Schema(name="TblAccountMySql")
public class TblAccountMySql {

    private static final long serialVersionUID = 3942181650986783670L;
    @TableId("ACCID")
    private String accId;
    @Transient
    private String accNameOne;
    @TableField("DC")
    private String dc;
    @TableField("HIGHACCID")
    private String highAccId;
    @TableField("TYPEID")
    private String typeId;
    @Transient
    private String accNameTwo;
    @TableField("IGRADE")
    private Integer igrade;
    @TableField("SICASH")
    private Integer sicash;
    @TableField("ISBZKM")
    private Integer isbzkm;
    @TableField("ISDCACC")
    private Integer isdcacc;
    @TableField("DES")
    private String des;
    @TableField("AYEAR")
    private Integer ayear;
    //HBFKCWZT
}
