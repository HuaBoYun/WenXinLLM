package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@TableName("TBL_ASSESS_TARGET")
@Schema(name="TblAssessTarget对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblAssessTarget implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.INPUT)
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

    public BigDecimal getAssid() {
        return assid;
    }

    public void setAssid(BigDecimal assid) {
        this.assid = assid;
    }
    public BigDecimal getOrgid() {
        return orgid;
    }

    public void setOrgid(BigDecimal orgid) {
        this.orgid = orgid;
    }
    public Float getFinalscore() {
        return finalscore;
    }

    public void setFinalscore(Float finalscore) {
        this.finalscore = finalscore;
    }
    public String getChecklevel() {
        return checklevel;
    }

    public void setChecklevel(String checklevel) {
        this.checklevel = checklevel;
    }
    public String getCheckreason() {
        return checkreason;
    }

    public void setCheckreason(String checkreason) {
        this.checkreason = checkreason;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public BigDecimal getAssesstargetid() {
        return assesstargetid;
    }

    public void setAssesstargetid(BigDecimal assesstargetid) {
        this.assesstargetid = assesstargetid;
    }
    public String getFinallevel() {
        return finallevel;
    }

    public void setFinallevel(String finallevel) {
        this.finallevel = finallevel;
    }

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
