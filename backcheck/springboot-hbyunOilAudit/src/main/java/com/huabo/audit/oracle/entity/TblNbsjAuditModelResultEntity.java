package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_AUDITMODELRESULT")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjAuditModelResultEntity {
//	private static final long serialVersionUID = 1L;
	
	@TableId(value = "resultid", type= IdType.AUTO)
    @Schema
    private BigDecimal resultId;
	
	@TableId(value = "savetime")
    @Schema
    private Date saveTime;
	
	@TableId(value = "signid")
    @Schema
    private String signId;
	
	@TableId(value = "tblstaff")
    @Schema
    private TblStaff tblStaff;
	
	@TableId(value = "monitormodel")
    @Schema
    private TblNbsjAuditModelEntity monitorModel;
	
	@TableId(value = "indexsql")
    @Schema
    private Integer indexSql;
	
	@TableId(value = "isdata")
    @Schema
    private Integer isData;
	
	@TableId(value = "solutionresultid")
    @Schema
    private Integer solutionResultid;
	
}
