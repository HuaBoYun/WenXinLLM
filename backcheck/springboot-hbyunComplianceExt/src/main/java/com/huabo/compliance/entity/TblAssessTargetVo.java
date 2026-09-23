package com.huabo.compliance.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author：yhr
 * @date:2022-08-31 14:12
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name="TblAssessTargetVo对象")
public class TblAssessTargetVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="评价结果Id")
    private BigDecimal assesstargetid;

    //TBL_ASSESS 外键
    @Schema(name="评价立项Id")
    private BigDecimal assid;

    // TBL_ORGANIZATION外键
    @Schema(name="被评价对象")
    private BigDecimal orgid;

    @Schema(name="初步评分")
    private Float finalscore;

    @Schema(name="校正级别")
    private String checklevel;

    @Schema(name="校正原因")
    private String checkreason;

    @Schema(name="状态")
    private String status;

    @Schema(name="初步级别")
    private String finallevel;

    @Schema(name="评价对象名称")
    private String  orgname;

    @Schema(name="评价负责人id")
    private BigDecimal staffid;

    @Schema(name="负责人名字")
    private String realname;

    @Schema(name="负责人登录名")
    private String username;


    @Schema(name="评价项目名称")
    private String assessname;

    @Schema(name="评价项目编号")
    private String assessid;

    @Schema(name="评价日期")

    private LocalDateTime assessdate;

    @Schema(name="计算状态")
    private  String  state;


    @Schema(name="风险发现集合")
    private Set<TblNbkzRisk> tblTargetRisks=new HashSet<TblNbkzRisk>();
    @Schema(name="底稿集合")
    private Set<TblWorksheet> tblTargetSheets=new HashSet<TblWorksheet>();
    @Schema(name="缺陷集合")
    private Set<TblBug> tblTargetBugs=new HashSet<TblBug>();




}
