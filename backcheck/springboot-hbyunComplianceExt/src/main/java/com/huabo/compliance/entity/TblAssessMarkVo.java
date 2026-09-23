package com.huabo.compliance.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblAssessMarkVo implements Serializable {

    private static final long serialVersionUID = 1L;


    private BigDecimal assmarkid;

    private BigDecimal score;

    private String memo;
    @Schema(name="适用性(1适用 0不适用)")
    private String suitable;

    //TBL_ASSESSELEMENT 外键
    private BigDecimal asseleid;

    //TBL_STAFF 外键
    @Schema(name="主评人id")
    private BigDecimal staffid;

    @Schema(name="主评人名字")
    private String realname;


    @Schema(name="负责人登录名")
    private String username;


    @Schema(name="评价要素")
    private String elementname;

    @Schema(name="状态")
    private String state = "1";
    //TBL_ASSESS 外键
    private Integer assid;

    //TBL_ORGANIZATION 外键
    private BigDecimal assorgid;

    //TBL_ASSESS_TARGET 外键
    private BigDecimal assesstargetid;

    @Schema(name="评价对象名称")
    private String  orgname;
    @Schema(name="参评人集合")
    LinkedHashSet<TblAssessStaffVo> canpingrens;

}
