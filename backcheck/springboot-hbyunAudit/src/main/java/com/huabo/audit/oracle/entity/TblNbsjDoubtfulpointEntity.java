package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@TableName("TBL_NBSJ_DOUBTFULPOINT")
@Data
@Schema(name="疑点实体类")
@Accessors(chain = true)
//@KeySequence(value="HIBERNATE_SEQUENCE",clazz=Integer.class) //value为数据库中生成的序列名，class指主键属性类型
public class TblNbsjDoubtfulpointEntity {


    @Schema(name = "主键")
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @TableId(value = "dpointid",type = IdType.INPUT)
    private BigDecimal dpointid;

    @TableField(value = "dpnumber")
    @Schema(name = "疑点编号")
    private String dpnumber;
    
    @TableField(value = "editor")
    @Schema(name="编制人",hidden=true)
    @IgnoreSwaggerParameter
    private String editor;
    
    @TableField(value = "CREATESTAFFID")
    @Schema(name="编制人id",hidden=true)
	@IgnoreSwaggerParameter
    private BigDecimal createstaffid;
    
    @TableField(value = "edittime")
    @Schema(name="编制时间",hidden=true)
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date edittime;
    
	@TableField(value = "dpdescribe")
    @Schema(name = "疑点描述")
    private String dpdescribe;

	@TableField(value = "memo")
    @Schema(name = "备注")
    private String memo;
	
	@TableField(value = "dpname")
    @Schema(name = "疑点名称")
    private String dpname;
	
	@TableField(value = "testresult")
    @Schema(name = "测试结果")
    private String testresult;
	
	@TableField(value = "dpstatus")
    @Schema
    private String dpstatus;
	
	@TableField(value = "dpbysystem")
    @Schema
    private String dpbysystem;
	
	@TableField(value = "orgid")
    @Schema(name="隶属组织id",hidden=true)
	@IgnoreSwaggerParameter
    private BigDecimal orgid;
	
	@TableField(value = "projectid")
    @Schema(name = "关联项目id")
    private BigDecimal projectid;
	
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
	
	
}
