package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="系统表单信息表", description="")
@Table(name = "TBL_SYSTEM_FORMFLOW")
public class TblSystemFormFlow implements Serializable {

	private static final long serialVersionUID = -5662394284640994890L;

	@Schema(name="业务数据表主键")
    @TableId(value="FORMID",type = IdType.INPUT)
    @Column(name="FORMID")
    private String formId;

    @TableField("FLOWID")
    @Schema(name="流程主键")
    @Column(name="FLOWID")
    private String flowId;

    @TableField("YMFORMID")
    @Schema(name="流程任务主键")
    @Column(name="YMFORMID")
    private String ymFormId;
    
    @Schema(name="流程发起人主键")
    @TableField("USERID")
    @Column(name="USERID")
    private BigDecimal userId;
    
    @Schema(name="流程发起公司")
    @TableField("ORGID")
    @Column(name="ORGID")
    private BigDecimal orgId;
    
    @Schema(name="数据类型")
    @TableField("TABLEID")
    @Column(name="TABLEID")
    private BigDecimal tableId;
    
    @Schema(name="流程发起部门主键")
    @TableField("DEPTID")
    @Column(name="DEPTID")
    private BigDecimal deptId;
}
