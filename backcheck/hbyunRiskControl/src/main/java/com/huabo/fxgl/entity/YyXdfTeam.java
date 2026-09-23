package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;

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
@TableName("TBL_YY_XDF_TEAM")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class YyXdfTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private BigDecimal teamid;

    private String teamname;

    private LocalDateTime createdate;

    private BigDecimal staffid;

    private BigDecimal companyid;

    public BigDecimal getTeamid() {
        return teamid;
    }

    public void setTeamid(BigDecimal teamid) {
        this.teamid = teamid;
    }
    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }
    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public BigDecimal getCompanyid() {
        return companyid;
    }

    public void setCompanyid(BigDecimal companyid) {
        this.companyid = companyid;
    }

    @Override
    public String toString() {
        return "YyXdfTeam{" +
            "teamid=" + teamid +
            ", teamname=" + teamname +
            ", createdate=" + createdate +
            ", staffid=" + staffid +
            ", companyid=" + companyid +
        "}";
    }
}
