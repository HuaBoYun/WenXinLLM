package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @since 2022-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_XDF_TEAM")
@Schema(name="TblYyXdfTeam对象")
public class TblYyXdfTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "TEAMID",type = IdType.INPUT)
    private BigDecimal teamid;

    @TableField("TEAMNAME")
    private String teamname;

    @TableField("CREATEDATE")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdate;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("COMPANYID")
    private BigDecimal companyid;

    @TableField(exist = false , select = false , fill=FieldFill.DEFAULT)
    private Integer copanycount;


}
