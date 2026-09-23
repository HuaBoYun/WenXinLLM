package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
@TableName("TBL_TESTTEMPLE")
@Schema(name="TblTesttemple对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblTesttemple implements Serializable {



    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    @Schema(name="Id")
    private BigDecimal testtemid;

    @Schema(name="模板名称")
    private String templename;//模板名称

    @Schema(name="模板编号")
    private String templenumber;//模板编号

    @Schema(name="备注")
    private String memo;

    @Schema(name="创建时间")
    private LocalDateTime createtime;//创建时间

    @Schema(name="状态")
    private BigDecimal templestatus;

    @Schema(name="模板说明")
    private String templedesc;//模板说明

    @Schema(name="隶属组织")
    private String tblcomany;//公司名称

    @Schema(name="创建人")
    private BigDecimal staffid;//创建者id

    @Schema(name="来源")
    private String source;//来源：自建、上级公司名称

    public BigDecimal getTesttemid() {
        return testtemid;
    }

    public void setTesttemid(BigDecimal testtemid) {
        this.testtemid = testtemid;
    }
    public String getTemplename() {
        return templename;
    }

    public void setTemplename(String templename) {
        this.templename = templename;
    }
    public String getTemplenumber() {
        return templenumber;
    }

    public void setTemplenumber(String templenumber) {
        this.templenumber = templenumber;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
 
    public BigDecimal getTemplestatus() {
        return templestatus;
    }

    public void setTemplestatus(BigDecimal templestatus) {
        this.templestatus = templestatus;
    }
    public String getTempledesc() {
        return templedesc;
    }

    public void setTempledesc(String templedesc) {
        this.templedesc = templedesc;
    }
    public String getTblcomany() {
        return tblcomany;
    }

    public void setTblcomany(String tblcomany) {
        this.tblcomany = tblcomany;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getCreatetime() {
		return createtime;
	}

	public void setCreatetime(LocalDateTime createtime) {
		this.createtime = createtime;
	}

	@Override
    public String toString() {
        return "TblTesttemple{" +
            "testtemid=" + testtemid +
            ", templename=" + templename +
            ", templenumber=" + templenumber +
            ", memo=" + memo +
            ", createtime=" + createtime +
            ", templestatus=" + templestatus +
            ", templedesc=" + templedesc +
            ", tblcomany=" + tblcomany +
            ", staffid=" + staffid +
            ", source=" + source +
        "}";
    }
}
