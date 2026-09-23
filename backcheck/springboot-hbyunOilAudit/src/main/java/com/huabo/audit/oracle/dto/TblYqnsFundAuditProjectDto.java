package com.huabo.audit.oracle.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.dto.base.BaseProject;
import com.huabo.audit.oracle.entity.FundAuditProjectAttEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-15 08:20
 **/
@Data
public class TblYqnsFundAuditProjectDto extends BaseProject {

    /**
     * 项目名称
     */
    @Schema(name = "项目名称")
    private String name;

    /**
     * 小组
     */
    @Schema(name = "小组")
    private String auditGroup;

    /**
     * 审计范围
     */
    @Schema(name = "审计范围")
    private String auditRange;

    

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
     * 计划id
     */
    @Schema(name = "计划id")
    private String jhId;
    
    
    
    @Schema(name = "关联计划项目id")
    private Long gljhxmid;
    
    @Schema(name = "关联计划项目类型")
    private String gljhxmlx;
    
    
    @Schema(name = "关联计划id")
    private Long planid;
    

    @Schema(name = "关联计划名称")
    private String planname;
    
    @Schema(name = "备注")
    private String remarks;
    
    
    @Schema(name = "编号")
    private String code;
    
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
    
    
    @Schema(name = "选择计划项目子项ID集合")
    private String ids;
    
    @Schema(name = "类别区分 1 上报人员保存 ")
    private Integer qftype;
    
    @Schema(name = "下发状态（1已上报 0或null 未上报）")
    private Integer xfstatus;
    
    private List<FundAuditProjectAttEntity> fundAuditProjectAttEntityList;

}
