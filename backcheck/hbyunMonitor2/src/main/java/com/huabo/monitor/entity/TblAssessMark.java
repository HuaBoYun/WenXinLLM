package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Data
@TableName("TBL_ASSESS_MARK")
@Schema(name="TblAssessMark对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblAssessMark implements Serializable {

    public static String START = "2";
    public static String UNSTART = "1";
    public static String SUM ="4";

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //TBL_ASSESS_STAFF 外键 一对多
    @TableId(type= IdType.INPUT)
    @Schema(name="主键")
    @TableField("ASSMARKID")
    private BigDecimal assmarkid;

    @Schema(name="评价分数")
    @TableField("SCORE")
    private BigDecimal score;

    @Schema(name="备注")
    @TableField("MEMO")
    private String memo;
    
    @Schema(name="适用性(1适用 0不适用)")
    @TableField("SUITABLE")
    private String suitable;

    //TBL_ASSESSELEMENT 外键
    @Schema(name="关联任务ID")
    @TableField("ASSELEID")
    private BigDecimal asseleid;

    //TBL_STAFF 外键
    @Schema(name="创建人")
    @TableField("STAFFID")
    private BigDecimal staffid;

    @Schema(name="状态")
    private String state = "1";
    
    
      //TBL_ASSESS 外键
    @Schema(name="立项ID")
    @TableField("ASSID")
    private Integer assid;

    //TBL_ORGANIZATION 外键
    @Schema(name="评价对象ID")
    @TableField("ASSORGID")
    private BigDecimal assorgid;

    //TBL_ASSESS_TARGET 外键
    @Schema(name="评价类别ID")
    @TableField("ASSESSTARGETID")
    private BigDecimal assesstargetid;

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getSuitable() {
        return suitable;
    }

    public void setSuitable(String suitable) {
        this.suitable = suitable;
    }
    public BigDecimal getAsseleid() {
        return asseleid;
    }

    public void setAsseleid(BigDecimal asseleid) {
        this.asseleid = asseleid;
    }
    public BigDecimal getAssmarkid() {
        return assmarkid;
    }

    public void setAssmarkid(BigDecimal assmarkid) {
        this.assmarkid = assmarkid;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public Integer getAssid() {
        return assid;
    }

    public void setAssid(Integer assid) {
        this.assid = assid;
    }
    public BigDecimal getAssorgid() {
        return assorgid;
    }

    public void setAssorgid(BigDecimal assorgid) {
        this.assorgid = assorgid;
    }
    public BigDecimal getAssesstargetid() {
        return assesstargetid;
    }

    public void setAssesstargetid(BigDecimal assesstargetid) {
        this.assesstargetid = assesstargetid;
    }

    @Override
    public String toString() {
        return "TblAssessMark{" +
            "score=" + score +
            ", memo=" + memo +
            ", suitable=" + suitable +
            ", asseleid=" + asseleid +
            ", assmarkid=" + assmarkid +
            ", staffid=" + staffid +
            ", state=" + state +
            ", assid=" + assid +
            ", assorgid=" + assorgid +
            ", assesstargetid=" + assesstargetid +
        "}";
    }
}
