package com.huabo.audit.oracle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-15 08:48
 **/
@Data
public class FundAuditProjectAttDto {

    /**
     * 财务审计项目附件主键
     */
    @Schema(name = "财务审计项目附件主键")
    private Long id;

    /**
     * 财务审计项目主键ID
     */
    @Schema(name = "财务审计项目主键ID")
    private Long fundAuditProjectId;

    /**
     * 附件ID
     */
    @Schema(name = "附件ID")
    private String attachmentid;

    /**
     * 附件名称
     */
    @Schema(name = "附件名称")
    private String attname;

    /**
     * 附件大小
     */
    @Schema(name = "附件大小")
    private double attsize;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    private String uploader;
    /**
     * 附件ID
     */
    @Schema(name = "附件ID")
    private BigDecimal attid;

}
