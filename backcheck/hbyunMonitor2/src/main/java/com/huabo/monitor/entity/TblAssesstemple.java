package com.huabo.monitor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hbfk.entity.TblOrganizationUtil;

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
@TableName("TBL_ASSESSTEMPLE")
@Schema(name="TblAssesstemple对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
@Data
public class TblAssesstemple extends FlexibleFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //TBL_ASSESS 外键
    @TableId(value="ASSTEMID",type= IdType.INPUT)
    @Schema(name="Id")
    private BigDecimal asstemid;
    
    @TableField("TEMPLENAME")
    @Schema(name="模板名称")
    private String templename;

    //TBL_ORGANIZATION 外键
    @TableField("ORGID")
    @Schema(name="隶属组织")
    private BigDecimal orgid;

    @TableField("MEMO")
    @Schema(name="备注")
    private String memo;

    @TableField("TEMPLENUMBER")
    @Schema(name="模板编号")
    private String templenumber;

    //TBL_STAFF 外键
    @TableField("STAFFID")
    @Schema(name="创建人")
    private BigDecimal staffid;

   // private Date modifydatetime;

    @TableField("TEMPLESTATUS")
    @Schema(name="模板状态")
    private Integer templestatus;
    
    
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
	@TableField("CREATETIME")
	@Column(name = "CREATETIME")
	@Schema(name = "创建时间")
	private Date createtime;
    
    

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
	@TableField("TEMPLEDES")
	@Schema(name="模板说明")
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
    @Schema(name="修改时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
