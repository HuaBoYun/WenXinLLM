package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import org.springframework.data.annotation.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author Rui
 * @ClassName ScoreManageEntity
 * @Description
 * @DATE 2023/10/9
 */
@Data
@TableName("TBL_YQNS_SCORE_MANAGE")
@Schema(name="评议管理-评分管理-评分标准")
@Accessors(chain = true)
public class ScoreManageItemEntity implements Serializable {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;


    @TableField(value="SMID")
    @Schema(name="TBL_YQNS_SCORE_MANAGE表对应ID，外键关联")
    @Transient
    @IgnoreSwaggerParameter
    private BigDecimal smId;

    @TableField(value="SORT")
    @Schema(name="排序")
    @Transient
    @IgnoreSwaggerParameter
    private Integer sort;

    @TableField(value="SCORE")
    @Schema(name="分值")
    private Double score;

    @TableField(value="SCORE_CONTENT")
    @Schema(name="评分标准")
    private String scoreContent;

}
