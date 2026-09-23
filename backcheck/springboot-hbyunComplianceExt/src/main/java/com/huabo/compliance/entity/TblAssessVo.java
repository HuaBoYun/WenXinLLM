package com.huabo.compliance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author：yhr
 * @date:2022-08-31 08:42
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblAssessVo implements Serializable {


    private static final long serialVersionUID = 1L;

    @Schema(name="评价项目ID")
    private BigDecimal assid;

    @Schema(name="评价项目编号")
    private String assessid;

    @Schema(name="开始时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime startdate;

    @Schema(name="结束是时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime enddate;

    @Schema(name="备注")
    private String memo;

    @Schema(name="状态(1创建2启动)")
    private String assstatus;

    @Schema(name="发起日期")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime assstartday;

    @Schema(name="评价组织")
    private String assorgs;

    @Schema(name="评价项目名称")
    private String assessname;

    @Schema(name="评价对象")
    private String asssponsor;

    @Schema(name="归档人")
    private String archiveperson;

    @Schema(name="归档时间")
    private LocalDateTime archivetime;

    @Schema(name="初步评价等级")
    private String preliminaryasslevel;

    @Schema(name="初步评价评分")
    private BigDecimal preliminaryassscore;

    @Schema(name="校正级别")
    private String adustlevel;

    @Schema(name="校正原因")
    private String adjustreson;

    private String analysissummary;

    private BigDecimal finalscore;

    //TBL_ASSESSTEMPLE 外键
    private BigDecimal asstemid;
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private LocalDateTime assessdate;

    //TBL_STAFF 外键
    @Schema(name="评价负责人")
    private BigDecimal leaderid;

    private String tblcomany;
     //模板名称
    private String  templename;

    @Schema(name="负责人名字")
    private String realname;

    @Schema(name="负责人登录名")
    private String username;


    @Schema(name="评价对象名称")
    private String  orgname;
    @Schema(name="被评价对象")
    private BigDecimal orgid;

}
