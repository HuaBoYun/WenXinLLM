package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author：yhr
 * @date:2022-08-29 11:43
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblAssessStaffVo  implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.INPUT)
    @TableField("ASSSTAFFID")
    private BigDecimal assstaffid;

    @Schema(name="参评人权重")
    @TableField("ASSWEIGHT")
    private BigDecimal assweight;

    @TableField("MEMO")
    private String memo;

    @Schema(name="参评人id")
    @TableField("STAFFID")
    private BigDecimal staffid;
    
    @Schema(name="参评人名字")
    @TableField("REALNAME")
    private String realname;
    
    @Schema(name="参评人id")
    @TableField("USERNAME")
    private String username;

    @TableField("SCORE")
    private BigDecimal score;
    @TableField("REASON")
    private String reason;

    @TableField("ASSDATETIME")
    private LocalDateTime assdatetime;

    //TBL_ASSESSMARK 外键
    @TableField("ASSMARKID")
    private BigDecimal assmarkid;

    @TableField("MARKSTAFFID")
    private BigDecimal markstaffid;

    @TableField("MARKREALNAME")
    private String markrealname;

    private BigDecimal status = new BigDecimal(0);

    //TBL_ORGANIZATION 外键
    @TableField("ORGID")
    private BigDecimal orgid;

    @Schema(name="评价对象名称")
    @TableField("ORGNAME")
    private String  orgname;
    //TBL_ATTACHMENT
    @TableField("ATTID")
    private BigDecimal attid;

    @TableField("EXAMINATION")
    private String examination;

    @Schema(name="评价立项Id")
    @TableField("ASSID")
    private BigDecimal assid;
}
