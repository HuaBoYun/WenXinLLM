package com.huabo.audit.oracle.entity;

import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_FACTBOOK")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjFactbookEntity {
	
	public final static Integer STATUS_1 = 1; //未审批
	public final static Integer STATUS_2 = 2;//审批中
	public final static Integer STATUS_3 = 3;//需调整
	public final static Integer STATUS_4 = 4;//已终止
	public final static Integer STATUS_5 = 5;//已通过
	
	@TableId(value = "factid", type= IdType.AUTO)
    @Schema(name = "主键")
    private Integer factid;

    @TableField(value = "tblStaffByCreatestaffid")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private TblStaff tblStaffByCreatestaffid;

	@TableField(value = "tblNbsjProject")
    @Schema(hidden=true)
	@IgnoreSwaggerParameter
    private TblNbsjProject tblNbsjProject;
	
	@TableField(value = "tblStaffByFactstaffid")
    @Schema(hidden=true)
	@IgnoreSwaggerParameter
    private TblStaff tblStaffByFactstaffid;
	
	@TableField(value = "factcode")
	@Schema(name = "事实确认书编号")
	private String factcode;
	
	@TableField(value = "factname")
	@Schema(name = "事实确认书名称")
	private String factname;
	
	@TableField(value = "createtime")
	@Schema(hidden=true)
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date createtime;
	
	@TableField(value = "updatetime")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Date updatetime;
	
	@TableField(value = "status")
	@Schema
	private Integer status;
	
	@TableField(value = "describe")
	@Schema
	private String describe;
	
	@TableField(value = "tblNbsjQuestionaffirms")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set tblNbsjQuestionaffirms;
	
	@TableField(value = "tblNbsjFactbookatts")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Set<TblAttachment> tblNbsjFactbookatts;
	
	@TableField(value = "PROJECTID")
	@Schema(name = "项目id")
	private Integer projectid;
	
	@TableField(value = "CREATESTAFFID")
	@Schema(name = "拟稿人id")
	private Integer createstaffid;
	
	@TableField(value = "CREATESTAFFNAME")
	@Schema(name = "拟稿人名称")
	private String createstaffname;
	
	@TableField(value = "fhStaffid")
	@Schema(name = "复核人id")
	private Integer fhstaffid;
	
	@TableField(value = "FHSTAFFNAME")
	@Schema(name = "复核人姓名")
	private String fhstaffname;
	
	@TableField(value = "FACTSTAFFID")
	@Schema(name = "事实确认人id")
	private Integer factstaffid;
	
	@TableField(value = "FACTSTAFFNAME")
	@Schema(name = "事实确认人名称")
	private String factstaffname;
	
	
}
