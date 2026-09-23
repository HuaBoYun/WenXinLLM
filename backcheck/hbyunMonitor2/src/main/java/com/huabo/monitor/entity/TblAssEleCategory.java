package com.huabo.monitor.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.huabo.monitor.util.IgnoreSwaggerParameter;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Data
@TableName("TBL_ASSELE_CATEGORY")
@Schema(name="TblAsseleCategory对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblAssEleCategory extends FlexibleFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //TBL_ASSESSELEMENT 外键
    @Schema(name="外键")
    @TableField("ASSELEID")
    private BigDecimal asseleid;
    //TBL_ASSESSCATEGORY 外键
    @Schema(name="外键")
    @TableField("ASSCATID")
    private BigDecimal asscatid;
    //主键
    @TableId(value="ELEMENTCATEGORYID",type= IdType.INPUT)
    @Schema(name = "主键id")
    private BigDecimal elementcategoryid;

    @Schema(name="外键")
    @TableField("STANDARDSCORE")
    private BigDecimal standardscore = new BigDecimal(5);
    
    @TableField(exist=false)
    private TblAssesscategory assesscategory;
    @TableField(exist=false)
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
