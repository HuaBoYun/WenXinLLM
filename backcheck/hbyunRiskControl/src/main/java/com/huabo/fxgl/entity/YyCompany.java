package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-02
 */
@Data
@TableName(value = "TBL_YY_COMPANY", resultMap = "RM_YY_COMPANY")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class YyCompany implements Serializable {

    private static final long serialVersionUID = 1L;
    //公司IDYyCompany
    @TableId(type = IdType.INPUT)
    private BigDecimal companyid;
    //公司名称
    private String companyname;
    
    @TableField(value = "TEAMID")
    private String teamid;
    
    //创建时间
    private LocalDateTime createdate;
    //创建人
    @TableField(value = "STAFFID", property = "staff.staffid")
    private Staff staff;
    //组织机构 ORGID
    @TableField(value = "ORGID", property = "organization.orgid")
    private Organization organization;
    //报告 REPORTID
    @TableField(value = "REPORTID", property = "report.reportid")
    private YyReportModel report;
    //风险类别
    private String fxtype;
    //状态
    private BigDecimal cstatus;
    //监控外部数据列表
    @TableField(exist = false)
    private List<YyPrice> priceList;
    //风险监控类型
    private String fxjktype;

    //监控内部数据列表
    @TableField(exist = false)
    private Set<BiPage> biPageSet;
    
    @TableField(value = "")
    private String bipageStr;

}
