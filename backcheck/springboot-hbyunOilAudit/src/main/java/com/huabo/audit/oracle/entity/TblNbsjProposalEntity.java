package com.huabo.audit.oracle.entity;

import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hbfk.entity.TblAttachment;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_PROPOSAL")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjProposalEntity {

	public final static Integer NO_DEL=0;//未删除
	public final static Integer YE_DEL=1;//已删除

	@TableId(value = "proid", type= IdType.AUTO)
    @Schema(name = "项目ID")
    private Integer proid;

    @TableField(value = "tblstaff")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private TblStaff tblStaff;
    
    @TableField(value = "status")
    @Schema
    private Integer status;
    
    @TableField(value = "tblbbsjproject")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private TblNbsjProject tblNbsjProject;
    
    @TableField(value = "procode")
    @Schema
    private String procode;
    
    @TableField(value = "proname")
    @Schema
    private String proname;
    
    
    @TableField(value = "createtime")
    @Schema(name = "创建时间")
    private Date createTime;

    @TableField(value = "updatetime")
    @Schema(name = "修改时间")
    private Date updateTime;
    
    @TableField(value = "content")
    @Schema
    private String content;
    
    @TableField(value = "tblNbsjProposalatts")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private Set<TblAttachment> tblNbsjProposalatts;
    
    @TableField(value = "staffid")
    @Schema(hidden=true)
    private Integer staffid;
    
    @TableField(value = "projectId")
    @Schema(hidden=true)
    private Integer projectId;
}
