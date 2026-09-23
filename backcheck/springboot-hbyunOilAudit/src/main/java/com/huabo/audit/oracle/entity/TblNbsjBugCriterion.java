package com.huabo.audit.oracle.entity;

import javax.persistence.Id;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_BUGCRITERION")
@Data
@Schema(name="缺陷标准实体类")
@Accessors(chain = true)
public class TblNbsjBugCriterion {
	
	@Id
    @TableId(value = "BUGCRIID")
    @Schema(name = "缺陷标准ID")
    private Integer bugcriid;
	
	@TableField(value = "BUGCRILEVEL")
	@Schema(name = "缺陷级别")
	private String bugcrilevel;
	
	@TableField(value = "BUGCRIDEFINE")
	@Schema(name = "定义")
	private String bugcridefine;
	
	@TableField(value = "STATUS")
	@Schema(name = "状态  1禁用  2正常")
	private Integer status;
	
	@TableField(value = "BUGCRIRATION")
	@Schema(name = "定量标准")
	private String bugcriration;
	
	@TableField(value = "BUGCRISTABILITY")
	@Schema(name = "定性标准")
	private String bugcristability;
	
	@TableField(value = "VERSION")
	@Schema(name = "版本")
	private Integer version;
	
	@TableField(value = "ORGID")
	@Schema(name = "隶属组织")
	private Integer orgid;
	//private Set<TblNbsjBug> tblNbsjBugs;
	
}
