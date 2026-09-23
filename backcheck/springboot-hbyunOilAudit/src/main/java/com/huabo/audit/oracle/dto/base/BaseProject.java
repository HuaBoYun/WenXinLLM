package com.huabo.audit.oracle.dto.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: workspace
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-20 23:42
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseProject {

    private Long id;

    /**
     * 实施单位
     */
    @Schema(name = "实施单位", required = true)
    private String exePhraseUnit;

    /**
     * 组长
     */
    @Schema(name = "组长", required = true)
    private String groupLeader;

    /**
     * 主审
     */
    @Schema(name = "主审", required = true)
    private String approver;

    /**
     * 助审
     */
    @Schema(name = "助审", required = true)
    private String assistApprover;

    /**
     * 实施单位Id
     */
    @Schema(name = "实施单位Id", required = true)
    private Long exePhraseUnitId;

    /**
     * 组长Id
     */
    @Schema(name = "组长Id", required = true)
    private Long groupLeaderId;

    /**
     * 主审Id
     */
    @Schema(name = "主审Id", required = true)
    private Long approverId;

    /**
     * 助审Id
     */
    @Schema(name = "助审Id", required = true)
    private String assistApproverId;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @Schema(name = "创建人", required = true)
    private String creater;

    /**
     * 创建人ID
     */
    @Schema(name = "创建人ID", required = true)
    private Long createrId;

    @Schema(name = "状态")
    private Integer status;
    
    
    @Schema(name = "副组长主键")
    private BigDecimal fzzStafffId;
    
    @Schema(name = "副组长名称")
    private String fzzName;

}
