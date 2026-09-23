package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-03-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_CONTRACT_INFORMATION")
@Schema(name="TblContractInformation对象")
public class TblContractInformation implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "INFOID" ,type=IdType.INPUT)
      private BigDecimal infoid;

    @TableField("INFONO")
    private String infono;

    @TableField("INFONAME")
    private String infoname;

    @TableField("INFOTYPE")
    private String infotype;

    @TableField("INFOXH")
    private String infoxh;//规格

    @TableField("INFOORG")
    private String infoorg;

    @TableField("INFOPRICE")
    private BigDecimal infoprice;//单价

    @TableField("INFONUM")
    private BigDecimal infonum;//数量

    @TableField("INFODESC")
    private String infodesc;//说明

    @TableField("INFOMOMO")
    private String infomomo;//备注

    @TableField("INFOPINPAI")
    private String infopinpai;

    @TableField("INFOSTARTDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date infostartdate;

    @TableField("INFOENDDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date infoenddate;

    @TableField("PROJECTID")
    private BigDecimal projectid;//隶属合同


}
