package com.huabo.audit.oracle.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-09 16:12
 **/
@Data
public class TblYqnsSiteReviewAttEntityDto {

    /**
     * 现场审查主要内容附件关系主键
     */
    @Schema(name = "现场审查主要内容附件关系主键ID")
    private Long id;

    /**
     * 现场审查主要内容表主键
     */
    @Schema(name = "现场审查主要内容表主键")
    private Long siteReviewContentId;

    /**
     * 附件表ID
     */
    @Schema(name = "附件表ID")
    private String attachmentId;

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
     * 附件表ID
     */
    @Schema(name = "附件表ID")
    private BigDecimal attid;

}
