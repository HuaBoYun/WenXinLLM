package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 描述: 底稿
 * author: ziyao
 * date: 2022-04-20
 */
@TableName("TBL_NBSJ_TEMPLETE")
@Data
@Schema(name="审计模板实体类")
@Accessors(chain = true)
public class TblNbsjTempleteEntity  extends BaseReservedProperty {
	public static final Integer NORMAL = 1;  //启用
	public static final Integer UNNORMAL=-1; //禁用
	
	public static final Integer MB_TYPE = 0;   //审计模板
	public static final Integer ZY_TYPE = 1;  // 指引模板
	public static final Integer COPY_TYPE=2; // 审计经验库

    @TableId(value = "templeteid", type= IdType.AUTO)
    @Schema(name = "模板id")
    private BigDecimal templeteId;

    @TableField(value = "templetecode")
    @Schema(name = "模板编号")
    private String templeteCode;

    @TableField(value = "templetename")
    @Schema(name = "模板名称")
    private String templeteName;

    @TableField(value = "templetetype")
    @Schema(name = "审计类型", required = true)
    private String templeteType;
 
    @TableField(value = "templetedesc")
    @Schema(name = "模板说明")
    private String templeteDesc;

    @TableField(value = "staffid")
    @Schema(name="创建人id",hidden=true)
    private BigDecimal staffId;

    @TableField(value = "createdate")
    @Schema(name="创建日期",hidden=true)
    private Date createDate;

    @TableField(value = "updatedate")
    @Schema(name="修改日期",hidden=true)
    private Date updateDate;

    @TableField(value = "updatestaffid")
    @Schema(name="修改人id",hidden=true)
    private BigDecimal updateStaffId;

    @TableField(value = "status")
    @Schema(name="状态：启用：1、禁用“-1",hidden=true)
    private Integer status;

    @TableField(value = "temptype")
    @Schema(name = "模板类型：审计模板：0、指引模板：1、审计经验库：2", required = true)
    private String tempType;

    @TableField(value = "orgid")
    @Schema(name="模板所属公司id",hidden=true)
    private BigDecimal orgId;
    
    @Schema(name="涉及单位",hidden=true)
    @IgnoreSwaggerParameter
    private String temorgname;
    
    @Schema(name="涉及单位的id",hidden=true)
    @IgnoreSwaggerParameter
    private String temorgids;

    @IgnoreSwaggerParameter
    @Schema(name="创建人",hidden=true)
    private String createstaffname;
    
    @Schema
    @IgnoreSwaggerParameter
    private Set<TblOrganization> organizations;
    
}
