package com.huabo.compliance.entity;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("TBL_COM_EXT_TESTPLAN")
@Schema(name="TblTestplan对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblTestplanVo implements Serializable {



    private static final long serialVersionUID = 1L;

    @Schema(name="ID")
    @TableId(type = IdType.INPUT)
    private BigDecimal testplanid;

    @Schema(name="计划编号")
    private String plannumber;

    @Schema(name="计划名称")
    private String planname;

    @Schema(name="计划年度")
    private String planyear;

    @Schema(name="测试类型")
    private String testtype;

    private String planmadeorg;

    @Schema(name="计划制定部门")
    private String planmadedep;

    @Schema(name="计划开始时间")
    private LocalDateTime starttime;

    @Schema(name="计划结束时间")
    private LocalDateTime endtime;

    @Schema(name="负责人")
    private String planleader;

    @Schema(name="开展费用")
    private BigDecimal planfee;

    @Schema(name="投入人力")
    private String numberofpeople;

    @Schema(name="被测试机构")
    private String testedorgs;

    @Schema(name="备注")
    private String memo;

    @Schema(name="计划状态")
    private String planstatus;

    @Schema(name="计划制定部门id")
    private BigDecimal orgid;

    @Schema(name="创建人")
    private BigDecimal creatid;

    @Schema(name="负责人")
    private BigDecimal staffid;

    @Schema(name="测试模板")
    private BigDecimal testtemid;



    @Schema(name="退回状态")
    private BigDecimal returnstatus;

    private TblTesttemple  testtemple;

}
