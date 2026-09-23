package com.huabo.fxgl.entity;

import afu.org.checkerframework.checker.oigj.qual.O;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_YY_TEAM")
//@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class YyTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    /*未找到Oracle数据库中对应的序列，顾使用MP生成的主键用作为下一个team的主键*/
    @TableId(type = IdType.INPUT)
    private BigDecimal teamid;

    private String teamname;

    private LocalDateTime createdate;

//    private BigDecimal staffid;

    @TableField(value = "STAFFID", property = "staff.staffid", select = false)
    private Staff staff;

//    private BigDecimal companyid;
    @TableField(value = "COMPANYID", property = "organization.orgid", select = false)
    private Organization organization;

    @TableField(exist = false)
    private Long companyCount;//分组下公司总数
    //风险监控类型
    private String fxjktype;

    public YyTeam(String teamname, LocalDateTime createdate, BigDecimal staffid, BigDecimal orgid, Long companyCount,String fxjktype) {
        this.teamname = teamname;
        this.createdate = createdate;
        Staff staff = new Staff();
        staff.setStaffid(staffid);
        this.staff = staff;
        Organization organization = new Organization();
        organization.setOrgid(orgid);
        this.organization = organization;
        this.companyCount = companyCount;
        this.fxjktype = fxjktype;
    }
}
