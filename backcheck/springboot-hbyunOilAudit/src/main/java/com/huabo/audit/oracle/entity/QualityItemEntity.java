package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author Rui
 * @ClassName QualityItem
 * @Description
 * @DATE 2023/10/12
 */
@Data
@TableName("TBL_YQNS_QUALITY_ITEM")
@Schema(name="考核内容")
@Accessors(chain = true)
public class QualityItemEntity {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(exist = false)
    @Schema(name = "单项考核内容")
    private ScoreManageItemEntity smItem;

    @TableField(value="SCORE")
    @Schema(name="得分")
    private Double score;

    @TableField(value="DEDUCT_REASON")
    @Schema(name="扣分项")
    private String deductReason;

}
