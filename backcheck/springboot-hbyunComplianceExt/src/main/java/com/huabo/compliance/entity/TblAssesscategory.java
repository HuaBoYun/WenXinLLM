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
@TableName("TBL_ASSESSCATEGORY")
@Schema(name="TblAssesscategory对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblAssesscategory implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //TBL_ASSELE_CATEGORY 外键 一对多
    @TableId(type= IdType.INPUT)
    private BigDecimal asscatid;

    @TableField(exist=false)
    private TblAssesstemple tblassesstemple;
    
    

	public TblAssesstemple getTblassesstemple() {
		return tblassesstemple;
	}

	public void setTblassesstemple(TblAssesstemple tblassesstemple) {
		this.tblassesstemple = tblassesstemple;
	}

	private String catname;

    private double catweight;

    private String memo;

    //TBL_ASSESSCATEGORY 外键 一对多
    private BigDecimal fatherasscatid;

    //TBL_ASSESSTEMPLE 外键 多对一
    private BigDecimal asstemid;

    private Integer catnumber;

    private String catdes;

    public BigDecimal getAsscatid() {
        return asscatid;
    }

    public void setAsscatid(BigDecimal asscatid) {
        this.asscatid = asscatid;
    }
    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }
    
    

	public double getCatweight() {
		return catweight;
	}

	public void setCatweight(double catweight) {
		this.catweight = catweight;
	}

	public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getFatherasscatid() {
        return fatherasscatid;
    }

    public void setFatherasscatid(BigDecimal fatherasscatid) {
        this.fatherasscatid = fatherasscatid;
    }
    public BigDecimal getAsstemid() {
        return asstemid;
    }

    public void setAsstemid(BigDecimal asstemid) {
        this.asstemid = asstemid;
    }
    public Integer getCatnumber() {
        return catnumber;
    }

    public void setCatnumber(Integer catnumber) {
        this.catnumber = catnumber;
    }
    public String getCatdes() {
        return catdes;
    }

    public void setCatdes(String catdes) {
        this.catdes = catdes;
    }

    @Override
    public String toString() {
        return "TblAssesscategory{" +
            "asscatid=" + asscatid +
            ", catname=" + catname +
            ", catweight=" + catweight +
            ", memo=" + memo +
            ", fatherasscatid=" + fatherasscatid +
            ", asstemid=" + asstemid +
            ", catnumber=" + catnumber +
            ", catdes=" + catdes +
        "}";
    }
}
