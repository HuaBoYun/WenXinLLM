package com.huabo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("TBL_ASSESSCATEGORY")
@Schema(name="TblAssesscategory对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblAssesscategory extends FlexibleFieldEntity  implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //TBL_ASSELE_CATEGORY 外键 一对多
    @TableId(value="ASSCATID",type= IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal asscatid;

    @TableField(exist=false)
    private TblAssesstemple tblassesstemple;
    
    

	public TblAssesstemple getTblassesstemple() {
		return tblassesstemple;
	}

	public void setTblassesstemple(TblAssesstemple tblassesstemple) {
		this.tblassesstemple = tblassesstemple;
	}

	@Schema(name="评价类别名称")
	 @TableField("CATNAME")
	private String catname;

	@Schema(name="权重")
	@TableField("CATWEIGHT")
    private double catweight;

	@Schema(name="备注")
	@TableField("MEMO")
    private String memo;

    //TBL_ASSESSCATEGORY 外键 一对多
	@Schema(name="父级类别ID")
	@TableField("FATHERASSCATID")
    private BigDecimal fatherasscatid;

    //TBL_ASSESSTEMPLE 外键 多对一
	@Schema(name="关联模板ID")
	@TableField("ASSTEMID")
    private BigDecimal asstemid;

	@Schema(name="评价类别编号")
	@TableField("CATNUMBER")
    private Integer catnumber;

	@Schema(name="评价类别描述")
	@TableField("CATDES")
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
