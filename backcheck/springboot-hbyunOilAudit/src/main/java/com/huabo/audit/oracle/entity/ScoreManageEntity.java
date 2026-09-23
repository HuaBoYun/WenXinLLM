package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * @author Rui
 * @ClassName ScoreManageEntity
 * @Description
 * @DATE 2023/10/9
 */
@Data
@TableName("TBL_YQNS_SCORE_MANAGE")
@Schema(name="评议管理-评分管理")
@Accessors(chain = true)
public class ScoreManageEntity extends BaseReservedProperty implements Serializable {
 
    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;


    @TableField(value="TYPE")
    @Schema(name="评分类型：1.审计实施方案制定及执行2.底稿质量 3.报告质量 4.审计管理系统上线 5.奖惩事项")
    private Integer type;

    @TableField(value="SCORE")
    @Schema(name="分值")
    private Double score;

    @TableField(value="STATUS")
    @Schema(name="是否启用 1.启用 2.废弃")
    private Integer status;


    @TableField(value="CREATE_USER")
    @Schema(name="创建人")
    private TblStaff createUser;

    @TableField(value="CREATE_TIME")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField(exist = false)
    @Schema(name = "评分标准")
    private List<ScoreManageItemEntity> scoreItems;

}
