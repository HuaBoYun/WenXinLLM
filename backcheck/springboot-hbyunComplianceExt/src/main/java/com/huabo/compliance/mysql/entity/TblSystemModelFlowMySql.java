package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SYSTEM_MODELFLOW")
@Schema(name="TblSystemModelFlowIdMySql")
public class TblSystemModelFlowMySql implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId("MODELID")
    private Integer modelId;
    @TableId("FLOWID")
    private Integer flowId;
    @TableField("MODELNO")
    private Integer modelNo;
//
//	@Transient
//	private TblSystemModelFlow id;
//	@Transient
//	private TblFlow flow;
//	@Transient
//	private TblSystemModule module;

}
