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

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Rui
 * @ClassName QualityResultEntity
 * @Description
 * @DATE 2023/10/16
 */
@Data
@TableName("TBL_YQNS_QUALITY_RESULT")
@Table(name = "TBL_YQNS_QUALITY_RESULT")
@Schema(name="质量评议")
@Accessors(chain = true)
public class QualityResultEntity extends BaseReservedProperty {

    @Id
    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @Column(name = "NO")
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    @TableField(exist = false)
    @Schema(name="项目名称")
    private ImplementPlanEntity project;

    @Column(name = "PROJECT_ID")
    @TableField(value="PROJECT_ID")
    @Schema(name="项目ID")
    private String projectId;

    @Column(name = "START_TIME")
    @TableField(value="START_TIME")
    @Schema(name="实施时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @Column(name = "RESULT")
    @TableField(value="RESULT")
    @Schema(name="评议结果")
    private String result;

    @Column(name = "ASSESSOR_ID")
    @TableField(value="ASSESSOR_ID")
    @Schema(name="考核人id")
    private BigDecimal assessorId;

    @Column(name = "REVIEWER_ID")
    @TableField(value="REVIEWER_ID")
    @Schema(name="复核人id")
    private BigDecimal reviewerId;

    @TableField(exist = false)
    @Schema(name="考核人")
    private String assessor;

    @TableField(exist = false)
    @Schema(name="复核人")
    private String reviewer;

    @Column(name = "REMARK")
    @TableField(value="REMARK")
    @Schema(name="备注")
    private String remark;

    @Column(name = "SCORE")
//    @TableField(exist = false)
    @TableField(value="SCORE")
    @Schema(name = "评议得分")
    private Integer score;

    @TableField(exist = false)
    @Schema(name = "评议列表")
    private List<QualityEntity> qualities;

//    public Integer getScore(){
//        if(this.qualities != null && this.qualities.size() > 0){
//            AtomicInteger sum = new AtomicInteger(0);
//            this.qualities.stream().forEach(t->{
//                sum.getAndAdd(t.getScore());
//            });
//            return sum.get();
//        }
//        return 0;
//    }
}
