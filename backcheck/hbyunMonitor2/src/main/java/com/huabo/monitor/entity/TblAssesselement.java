package com.huabo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("TBL_ASSESSELEMENT")
@Schema(name="TblAssesselement对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblAssesselement extends FlexibleFieldEntity  implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //TBL_ASSESS_MARK 外键 一对多
    @TableId(value="ASSELEID",type= IdType.INPUT)
    @Schema(name="Id")
    private BigDecimal asseleid;

    @Schema(name="要素名称")
    private String elementname;

    @Schema(name="标准分数")
    private BigDecimal standardscore;

    @Schema(name="备注")
    private String memo;

    @Schema(name="业务类别")
    private String businesstype;

    @Schema(name="业务属性")
    private String businessattribute;

    @Schema(name="审查要点")
    private String auditpoint;

    @Schema(name="评分规则")
    private double assessrules;

    @Schema(name="状态")
    private String status;

    @Schema(name="要素编号")
    private String elementnumber;//要素编号

    @Schema(name="隶属组织")
    private String tblcomany;
    

    //密级及查询条件
    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围id")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;
    
    @Schema(name = "知悉范围名称")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
 
    @Schema(name = "所属部门")
    @TableField("LINKDEPTID")
    @Column(name = "LINKDEPTID")
  private BigDecimal linkdeptid;
    
    @Schema(name="创建人")
    @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;
    
    @TableField("CREATETIME")
 	@Column(name = "CREATETIME")
 	@Schema(name = "创建时间")
     private Date createtime;
    
     
//	public String getElementnumber() {
//		return elementnumber;
//	}

	public void setElementnumber(String elementnumber) {
		this.elementnumber = elementnumber;
	}
    public String getElementnumber() {
		return elementnumber;
	}

    public BigDecimal getAsseleid() {
        return asseleid;
    }

    public void setAsseleid(BigDecimal asseleid) {
        this.asseleid = asseleid;
    }
    public String getElementname() {
        return elementname;
    }

    public void setElementname(String elementname) {
        this.elementname = elementname;
    }
    public BigDecimal getStandardscore() {
        return standardscore;
    }

    public void setStandardscore(BigDecimal standardscore) {
        this.standardscore = standardscore;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }
    public String getBusinessattribute() {
        return businessattribute;
    }

    public void setBusinessattribute(String businessattribute) {
        this.businessattribute = businessattribute;
    }
    public String getAuditpoint() {
        return auditpoint;
    }

    public void setAuditpoint(String auditpoint) {
        this.auditpoint = auditpoint;
    }
    public double getAssessrules() {
        return assessrules;
    }

    public void setAssessrules(double assessrules) {
        this.assessrules = assessrules;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTblcomany() {
        return tblcomany;
    }

    public void setTblcomany(String tblcomany) {
        this.tblcomany = tblcomany;
    }
 
 

	public BigDecimal getSecrectLevelId() {
		return secrectLevelId;
	}

	public void setSecrectLevelId(BigDecimal secrectLevelId) {
		this.secrectLevelId = secrectLevelId;
	}

	public String getStaffScopeIds() {
		return staffScopeIds;
	}

	public void setStaffScopeIds(String staffScopeIds) {
		this.staffScopeIds = staffScopeIds;
	}

	public String getStaffScopeNames() {
		return staffScopeNames;
	}

	public void setStaffScopeNames(String staffScopeNames) {
		this.staffScopeNames = staffScopeNames;
	}

	public BigDecimal getLinkdeptid() {
		return linkdeptid;
	}

	public void setLinkdeptid(BigDecimal linkdeptid) {
		this.linkdeptid = linkdeptid;
	}

	public BigDecimal getCreatestaffid() {
		return createstaffid;
	}

	public void setCreatestaffid(BigDecimal createstaffid) {
		this.createstaffid = createstaffid;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Override
    public String toString() {
        return "TblAssesselement{" +
            "asseleid=" + asseleid +
            ", elementname=" + elementname +
            ", standardscore=" + standardscore +
            ", memo=" + memo +
            ", businesstype=" + businesstype +
            ", businessattribute=" + businessattribute +
            ", auditpoint=" + auditpoint +
            ", assessrules=" + assessrules +
            ", status=" + status +
            ", elementnumber=" + elementnumber +
            ", tblcomany=" + tblcomany +
        "}";
    }
    
    
    
}
