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
@TableName("TBL_ASSESS_TARGET")
@Schema(name="TblAssessTarget对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblAssessTarget implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.INPUT)
    @Schema(name="评价结果Id")
    @TableField("ASSESSTARGETID")
    private BigDecimal assesstargetid;

    //TBL_ASSESS 外键
    @Schema(name="评价立项Id")
    @TableField("ASSID")
    private BigDecimal assid;

    // TBL_ORGANIZATION外键
    @Schema(name="被评价对象")
    @TableField("ORGID")
    private BigDecimal orgid;

    @Schema(name="初步评分")
    @TableField("FINALSCORE")
    private Float finalscore;

    @Schema(name="校正级别")
    @TableField("CHECKLEVEL")
    private String checklevel;

    @Schema(name="校正原因")
    @TableField("CHECKREASON")
    private String checkreason;

    @Schema(name="状态")
    @TableField("STATUS")
    private String status;

    @Schema(name="初步级别")
    @TableField("FINALLEVEL")
    private String finallevel;
 
    @Override
    public String toString() {
        return "TblAssessTarget{" +
            "assid=" + assid +
            ", orgid=" + orgid +
            ", finalscore=" + finalscore +
            ", checklevel=" + checklevel +
            ", checkreason=" + checkreason +
            ", status=" + status +
            ", assesstargetid=" + assesstargetid +
            ", finallevel=" + finallevel +
        "}";
    }
}
