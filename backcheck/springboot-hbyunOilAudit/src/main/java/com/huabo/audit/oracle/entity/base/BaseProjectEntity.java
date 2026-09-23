package com.huabo.audit.oracle.entity.base;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: workspace
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-20 23:50
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseProjectEntity extends BaseEntity {

    /**
     * 实施单位
     */
    @Schema(name = "实施单位")
    @TableField("EXEPHRASEUNIT")
    private String exePhraseUnit;

    /**
     * 组长
     */
    @Schema(name = "组长")
    @TableField("GROUPLEADER")
    private String groupLeader;

    /**
     * 主审
     */
    @Schema(name = "主审")
    @TableField("APPROVER")
    private String approver;

    /**
     * 助审
     */
    @Schema(name = "助审")
    @TableField("ASSISTAPPROVER")
    private String assistApprover;

    /**
     * 实施单位Id
     */
    @Schema(name = "实施单位Id")
    @TableField("EXEPHRASEUNITID")
    private BigDecimal exePhraseUnitId;

    /**
     * 组长Id
     */
    @Schema(name = "组长Id")
    @TableField("GROUPLEADERID")
    private BigDecimal groupLeaderId;

    /**
     * 主审Id
     */
    @Schema(name = "主审Id")
    @TableField("APPROVERID")
    private BigDecimal approverId;

    /**
     * 助审Id
     */
    @Schema(name = "助审Id")
    @TableField("ASSISTAPPROVERID")
    private String assistApproverId;
    
    
   

}
