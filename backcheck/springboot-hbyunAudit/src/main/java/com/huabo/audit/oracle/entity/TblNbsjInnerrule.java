package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

@Data
@TableName("TBL_NBSJ_INNERRULE")
@Schema(name="TblNbsjInnerrule", description="管理制度")
@Accessors(chain = true)
public class TblNbsjInnerrule extends FlexibleFieldEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId("INNRULID")
	@Schema(name = "主键")
    @Id
    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
	private BigDecimal innrulid;

    @TableField("RULENAME")
	@Schema(name = "制度名称")
    private String rulename;

    @TableField("PUBLISHORG")
	@Schema(name = "发文机构,传入orgid")
    private String publishorg;

    @TableField("PUBLISHDATE")
	@Schema(name = "生效日期")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
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
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date takeeffecttime;

    @TableField("SUMMARYINFO")
	@Schema(hidden=true)
    private String summaryinfo;

    @TableField("ENTERINGPERSON")
	@Schema(hidden=true)
    private String enteringperson;

    @TableField("enteringtime")
	@Schema(hidden=true)
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date enteringtime;

    @TableField("TIMELINESS")
	@Schema(hidden=true)
    private String timeliness;

    @TableField("MEMO")
	@Schema(hidden=true)
    private String memo;

    @Schema(name = "发文机构名称")
    @TableField(/*value = "ORGNAME",*/exist = false)
    private String orgname;
    
    @TableField("ZDTYPE")
	@Schema(name = "制度类型")
    private String zdtype;

    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    @Column(name = "SECRECTLEVELID")
    private BigDecimal secrectLevelId;

    @Schema(name = "知悉范围 多个逗号分隔")
    @TableField("STAFFSCOPEIDS")
    @Column(name = "STAFFSCOPEIDS")
    private String staffScopeIds;

    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
    @TableField("STAFFSCOPENAMES")
    @Column(name = "STAFFSCOPENAMES")
    private String staffScopeNames;

    @Schema(name = "创建人 用户表外键")
    @Column(name = "CREATESTAFFID")
    @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;

    
}
