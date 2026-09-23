package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
//@TableName("TBL_NBSJ_INNERRULE")
@TableName("TBL_YJPT_INNERRULE")
@Schema(name="TblNbsjInnerrule", description="管理制度")
@Accessors(chain = true)
public class TblNbsjInnerrule implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId("INNRULID")
	@Schema(name = "主键")
	private BigDecimal innrulid;

    @TableField("RULENAME")
	@Schema(name = "制度名称")
    private String rulename;

    @TableField("PUBLISHORG")
	@Schema(name = "发文机构,传入orgid")
    private String publishorg;

    @TableField("PUBLISHDATE")
	@Schema(name="生效日期",hidden=true)
    private Date publishdate;

    @TableField("RULENUMBER")
	@Schema(name = "发文文号")
    private String rulenumber;

    @TableField("RULECODE")
	@Schema(name = "制度编号")
    private String rulecode;

    @TableField("INNRULETYPE")
	@Schema(name = "类别")
    private String innruletype;
    
    @TableField("STATUS")
	@Schema(name = "状态：草稿、发布待审核、已发布、发布审核拒绝、已修订、已废止、废止待审核、废纸审核拒绝")
    private String status;    
    
    @TableField("BODYINFO")
	@Schema(name = "正文")
    private String bodyinfo;
    
    @TableField("COMPANYID")
	@Schema(name="所属公司",hidden=true)
    private String companyid;
    
    @TableField("HOSTDEPARTMENT")
	@Schema(hidden=true)
    private String hostdepartment;

    @TableField("COORGANIZER")
	@Schema(hidden=true)
    private String coorganizer;

    @TableField("VERSION")
	@Schema(hidden=true)
    private String version;

    @TableField("TAKEEFFECTTIME")
	@Schema(hidden=true)
    private Date takeeffecttime;

    @TableField("SUMMARYINFO")
	@Schema(hidden=true)
    private String summaryinfo;

    @TableField("ENTERINGPERSON")
	@Schema(hidden=true)
    private String enteringperson;

    @TableField("ENTERINGTIME")
	@Schema(hidden=true)
    private Date enteringtime;

    @TableField("TIMELINESS")
	@Schema(hidden=true)
    private String timeliness;

    @TableField("MEMO")
	@Schema(hidden=true)
    private String memo;
    
    @TableField("ORGNAME")
	@Schema(name = "发文机构名称")
    private String orgname;
    
}
