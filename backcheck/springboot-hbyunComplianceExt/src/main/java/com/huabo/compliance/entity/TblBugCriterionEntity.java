package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Set;

@TableName("TBL_BUGCRITERION")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblBugCriterionEntity {
	
//	@Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")  
	@TableId(value = "BUGCRIID", type= IdType.INPUT)
    @Schema(name = "主键ID,自动增长")
    private BigDecimal bugcriid;
	
//	@TableId(value = "bugcrilecel")
    @Schema(name = "缺陷级别")
    private String bugcrilevel;
	
//	@TableId(value = "bugcridefine")
    @Schema(name = "缺陷定义")
    private String bugcridefine;
	
//	@TableId(value = "bugcrigation")
    @Schema(name = "定量标准")
    private String bugcriration;
	
//	@TableId(value = "bugcristability")
    @Schema(name = "定性标准")
    private String bugcristability;
	
//	@TableId(value = "status")
    @Schema(name = "缺陷状态")
    private Integer status;
	
//	@TableId(value = "version")
    @Schema
    private Integer version;
	
//	@TableId(value = "ORGID")
    @Transient
    @Schema
    private Integer orgid;
	@Transient
    @Schema
    private Set<TblBug> tblBugs;
	
}
