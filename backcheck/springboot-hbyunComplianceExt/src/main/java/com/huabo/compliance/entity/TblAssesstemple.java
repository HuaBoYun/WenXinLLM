package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hbfk.entity.TblOrganizationUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Column;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@TableName("TBL_ASSESSTEMPLE")
@Schema(name="TblAssesstemple对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblAssesstemple implements Serializable {

    private static final long serialVersionUID = 1L;

    //TBL_ASSESS 外键
    @TableId(type= IdType.INPUT)
    private BigDecimal asstemid;

    private String templename;

    //TBL_ORGANIZATION 外键
    private BigDecimal orgid;

    private String memo;

    private String templenumber;

    //TBL_STAFF 外键
    private Integer staffid;

   // private Date modifydatetime;

    private Integer templestatus;

    public TblOrganizationUtil getTblcompany() {
		return tblcompany;
	}

	public void setTblcompany(TblOrganizationUtil tblcompany) {
		this.tblcompany = tblcompany;
	}

	public Set<TblOrganization> getAuorganizations() {
		return auorganizations;
	}

	public void setAuorganizations(Set<TblOrganization> auorganizations) {
		this.auorganizations = auorganizations;
	}

	public Set<TblOrganization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Set<TblOrganization> organizations) {
		this.organizations = organizations;
	}
	//模板说明
    private String templedes;
    
    @TableField(exist=false)
    private String realname;
    
    
    public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	//评价类别
    @TableField(exist=false)
    private Set<TblAssess> tblAssesses = new HashSet<TblAssess>();
    @TableField(exist=false)
    private String reorgText;
    
  //给列表页面提供模板是否已经被适用的依据
    @TableField(exist=false)
    private Integer numbers;
     

	public Integer getNumbers() {
		return numbers;
	}

	public void setNumbers(Integer numbers) {
		this.numbers = numbers;
	}

	public String getReorgText() {
		return reorgText;
	}

	public void setReorgText(String reorgText) {
		this.reorgText = reorgText;
	}

	public Set<TblAssess> getTblAssesses() {
		return tblAssesses;
	}

	public void setTblAssesses(Set<TblAssess> tblAssesses) {
		this.tblAssesses = tblAssesses;
	}
    @TableField(exist=false)
	private String reorg;
    @TableField(exist=false)
    private TblStaff staff;//用户
    @Column(name="MODIFYDATETIME")
    private Date modifydatetime;//修改时间
    
    
    @TableField(exist=false)
    private TblOrganizationUtil tblcompany;
    @TableField(exist=false)
    private Set<TblOrganization> auorganizations = new HashSet<>();
    @TableField(exist=false)
    private Set<TblOrganization> organizations = new HashSet<>();
    
    


	public TblStaff getStaff() {
		return staff;
	}

	public void setStaff(TblStaff staff) {
		this.staff = staff;
	}


//Hu


	public String getReorg() {
		return reorg;
	}

	public void setReorg(String reorg) {
		this.reorg = reorg;
	}

	public BigDecimal getAsstemid() {
        return asstemid;
    }

    public void setAsstemid(BigDecimal asstemid) {
        this.asstemid = asstemid;
    }
    public String getTemplename() {
        return templename;
    }

    public void setTemplename(String templename) {
        this.templename = templename;
    }
    
    public String getMemo() {
        return memo;
    }

    public BigDecimal getOrgid() {
		return orgid;
	}

	public void setOrgid(BigDecimal orgid) {
		this.orgid = orgid;
	}

	public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getTemplenumber() {
        return templenumber;
    }

    public void setTemplenumber(String templenumber) {
        this.templenumber = templenumber;
    }
    public Integer getStaffid() {
        return staffid;
    }

    public void setStaffid(Integer staffid) {
        this.staffid = staffid;
    }
 

	public Date getModifydatetime() {
		return modifydatetime;
	}

	public void setModifydatetime(Date modifydatetime) {
		this.modifydatetime = modifydatetime;
	}

	public Integer getTemplestatus() {
        return templestatus;
    }

    public void setTemplestatus(Integer templestatus) {
        this.templestatus = templestatus;
    }
    public String getTempledes() {
        return templedes;
    }

    public void setTempledes(String templedes) {
        this.templedes = templedes;
    }

    @Override
    public String toString() {
        return "TblAssesstemple{" +
            "asstemid=" + asstemid +
            ", templename=" + templename +
            ", orgid=" + orgid +
            ", memo=" + memo +
            ", templenumber=" + templenumber +
            ", staffid=" + staffid +
            ", modifydatetime=" + modifydatetime +
            ", templestatus=" + templestatus +
            ", templedes=" + templedes +
        "}";
    }
}
