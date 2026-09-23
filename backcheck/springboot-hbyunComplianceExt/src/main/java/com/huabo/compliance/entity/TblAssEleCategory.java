package com.huabo.compliance.entity;


import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 *
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@TableName("TBL_ASSELE_CATEGORY")
@Schema(name="TblAsseleCategory对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblAssEleCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    //TBL_ASSESSELEMENT 外键
    private BigDecimal asseleid;
    //TBL_ASSESSCATEGORY 外键
    private BigDecimal asscatid;
    //主键
    @TableId(type= IdType.INPUT)
    private BigDecimal elementcategoryid;

    private BigDecimal standardscore = new BigDecimal(5);
    
    private TblAssesscategory assesscategory;
    private TblAssesselement assesselement;

    public TblAssesselement getAssesselement() {
        return assesselement;
    }

    public void setAssesselement(TblAssesselement assesselement) {
        this.assesselement = assesselement;
    }
    
    public TblAssesscategory getAssesscategory() {
		return assesscategory;
	}

	public void setAssesscategory(TblAssesscategory assesscategory) {
		this.assesscategory = assesscategory;
	}

	public BigDecimal getAsseleid() {
        return asseleid;
    }

    public void setAsseleid(BigDecimal asseleid) {
        this.asseleid = asseleid;
    }
    public BigDecimal getAsscatid() {
        return asscatid;
    }

    public void setAsscatid(BigDecimal asscatid) {
        this.asscatid = asscatid;
    }
    public BigDecimal getElementcategoryid() {
        return elementcategoryid;
    }

    public void setElementcategoryid(BigDecimal elementcategoryid) {
        this.elementcategoryid = elementcategoryid;
    }
    public BigDecimal getStandardscore() {
        return standardscore;
    }

    public void setStandardscore(BigDecimal standardscore) {
        this.standardscore = standardscore;
    }

    @Override
    public String toString() {
        return "TblAsseleCategory{" +
                "asseleid=" + asseleid +
                ", asscatid=" + asscatid +
                ", elementcategoryid=" + elementcategoryid +
                ", standardscore=" + standardscore +
                "}";
    }
}
