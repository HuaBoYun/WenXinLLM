package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("TBL_ASSESSLEVEL")
@Schema(name="TblAssesslevel对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblAssesslevel implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.INPUT)
    private BigDecimal asslevid;

    private String levelname;

    private String upperregiondes;

    private BigDecimal levelupper;

    private BigDecimal levellower;

    private String lowerregiondes;

    private String leveldes;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(name="日期")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifieddate;

    private String memo;

    private String tblcomany;

    public BigDecimal getAsslevid() {
        return asslevid;
    }

    public void setAsslevid(BigDecimal asslevid) {
        this.asslevid = asslevid;
    }
    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }
    public String getUpperregiondes() {
        return upperregiondes;
    }

    public void setUpperregiondes(String upperregiondes) {
        this.upperregiondes = upperregiondes;
    }
    public BigDecimal getLevelupper() {
        return levelupper;
    }

    public void setLevelupper(BigDecimal levelupper) {
        this.levelupper = levelupper;
    }
    public BigDecimal getLevellower() {
        return levellower;
    }

    public void setLevellower(BigDecimal levellower) {
        this.levellower = levellower;
    }
    public String getLowerregiondes() {
        return lowerregiondes;
    }

    public void setLowerregiondes(String lowerregiondes) {
        this.lowerregiondes = lowerregiondes;
    }
    public String getLeveldes() {
        return leveldes;
    }

    public void setLeveldes(String leveldes) {
        this.leveldes = leveldes;
    }
   
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public String getTblcomany() {
        return tblcomany;
    }

    public void setTblcomany(String tblcomany) {
        this.tblcomany = tblcomany;
    }

    @Override
    public String toString() {
        return "TblAssesslevel{" +
            "asslevid=" + asslevid +
            ", levelname=" + levelname +
            ", upperregiondes=" + upperregiondes +
            ", levelupper=" + levelupper +
            ", levellower=" + levellower +
            ", lowerregiondes=" + lowerregiondes +
            ", leveldes=" + leveldes +
            ", modifieddate=" + modifieddate +
            ", memo=" + memo +
            ", tblcomany=" + tblcomany +
        "}";
    }
}
