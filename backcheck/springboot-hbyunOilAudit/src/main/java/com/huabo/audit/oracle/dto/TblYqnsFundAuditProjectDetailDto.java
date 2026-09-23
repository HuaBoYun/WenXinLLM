package com.huabo.audit.oracle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-15 08:48
 **/
@Data
public class TblYqnsFundAuditProjectDetailDto {

    /**
     * 财务审计项目ID
     */
    @Schema(name = "财务审计项目ID")
    private Long id;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    /**
     * 删除标识：1是；0否
     */
    @Schema(name = "删除标识：1是；0否")
    private BigDecimal deleted;

    /**
     * 项目名称
     */
    @Schema(name = "项目名称")
    private String name;

    /**
     * 实施单位
     */
    @Schema(name = "实施单位")
    private String exePhraseUnit;

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
     * 组长
     */
    @Schema(name = "组长")
    private String groupLeader;

    /**
     * 主审
     */
    @Schema(name = "主审")
    private String approver;

    /**
     * 助审
     */
    @Schema(name = "助审")
    private String assistApprover;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    private String creater;

    /**
     * 创建人ID
     */
    @Schema(name = "创建人ID")
    private Long createrId;

    private List<FundAuditProjectAttDto> fundAuditProjectAttDtoList;

}
