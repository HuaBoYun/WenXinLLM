package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
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
 * @since 2021-10-23
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_INVESTMENT_PROJECT")
@Schema(name="TblInvestmentProject对象")
public class TblInvestmentProject implements Serializable {
// 基金库
    private static final long serialVersionUID = 1L;

    @TableId(value = "PROJECTID" , type = IdType.INPUT)
    @Schema(name = "项目主键")
    private String projectId;

    @TableField("PROJECTNO")
    @Schema(name = "项目编号")
    private String projectNo;
    
    @TableField("PROJECTNAME")
    @Schema(name = "项目名称")
    private String projectName;
 
}
