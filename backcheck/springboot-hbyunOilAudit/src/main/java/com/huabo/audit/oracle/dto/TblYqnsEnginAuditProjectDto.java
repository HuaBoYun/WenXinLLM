package com.huabo.audit.oracle.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.dto.base.BaseProject;
import com.huabo.audit.oracle.entity.TblYqnsEnginProjectAttEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @program: springboot-hbyunMonitor
 * @description: 工程项目审计安排
 * @author: WangZhenDong
 * @create: 2023-10-14 10:37
 **/
@Data
public class TblYqnsEnginAuditProjectDto extends BaseProject {

    /**
     * 审计项目名称
     */
    @Schema(name = "审计项目名称")
    private String name;

    /**
     * 审计组
     */
    @Schema(name = "小组")
    private String auditGroup;

    /**
     * 被审计单位
     */
    @Schema(name = "被审计单位")
    private String auditUnit;

    /**
     * 被审计单位Id
     */
    @Schema(name = "被审计单位ID")
    private Long auditUnitId;

    /**
     * 项目数量
     */
    @Schema(name = "项目数量")
    private BigDecimal projectNum;

    /**
     * 金额,单位元
     */
    @Schema(name = "金额,单位元")
    private Double amount;
    
    
    @Schema(name = "关联计划项目id")
    @TableField("GLJHXMID")
    private Long gljhxmid;
    
    @Schema(name = "关联计划项目类型")
    @TableField("GLJHXMLX")
    private String gljhxmlx;
    
    
    @Schema(name = "关联计划id")
    @TableField("PLANID")
    private Long planid;
    

    @Schema(name = "关联计划名称")
    @TableField("PLANNAME")
    private String planname;

    @Schema(name = "编号")
    private String code;
    
    @Schema(name = "备注")
    private String remarks;
    
    
    @Schema(name = "现在结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xcsrarttime;

    
    @Schema(name = "现在结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xcendtime;
    
    
    @Schema(name = "人员数量")
    private Integer rsyq;
    
    @Schema(name = "项目数量")
    private Integer xmsl;
    
    

    private List<TblYqnsEnginProjectAttEntity> enginProjectAttEntityList;

}
