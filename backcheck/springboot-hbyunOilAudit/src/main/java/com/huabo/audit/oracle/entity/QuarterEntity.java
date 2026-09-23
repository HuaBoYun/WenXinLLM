package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author Rui
 * @ClassName QuarterEntity
 * @Description
 * @DATE 2023/12/10
 */

@Data
@TableName("TBL_YQNS_QUARTER")
@Schema(name="季度表")
@Accessors(chain = true)
public class QuarterEntity {

    @TableId(value="ID", type= IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(value="QUARTER")
    @Schema(name="季度 用1，2，3，4表示四个季度")
    private Integer quarter;

    @TableField(value="TYPE")
    @Schema(name="分类：1 三类 2 四类 ,三类随时上报，四类每季度最后15天可以进行上报")
    private Integer type;

    @TableField(value="ORG_ID")
    @Schema(name="填报单位")
    private BigDecimal orgId;

    @TableField(exist = false)
    @Schema(name="填报单位")
    private TblOrganization org;

    @TableField(exist = false)
    @Schema(name="填报单位名称")
    private String orgName;

    @TableField(value="CREATE_USER")
    @Schema(name="创建人")
    private TblStaff createUser;

    @TableField(value="CREATE_TIME")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

}
