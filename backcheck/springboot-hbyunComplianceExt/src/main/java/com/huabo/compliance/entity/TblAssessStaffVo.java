package com.huabo.compliance.entity;

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
    private BigDecimal assstaffid;

    @Schema(name="参评人权重")
    private BigDecimal assweight;

    private String memo;

    @Schema(name="参评人id")

    private BigDecimal staffid;
    @Schema(name="参评人名字")
    private String realname;
    @Schema(name="参评人id")
    private String username;

    private BigDecimal score;

    private String reason;

    private LocalDateTime assdatetime;

    //TBL_ASSESSMARK 外键
    private BigDecimal assmarkid;

    private BigDecimal markstaffid;

    private String markrealname;

    private BigDecimal status = new BigDecimal(0);

    //TBL_ORGANIZATION 外键
    private BigDecimal orgid;

    @Schema(name="评价对象名称")
    private String  orgname;
    //TBL_ATTACHMENT
    private BigDecimal attid;

    private String examination;

    @Schema(name="评价立项Id")
    private BigDecimal assid;
}
