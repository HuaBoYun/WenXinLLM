package com.huabo.audit.oracle.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: workspace
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-20 23:53
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseEntity {

    /**
     * 主键ID
     */
    @TableId(value = "ID")
    @Schema(name = "主键ID")
    @Column(name = "ID")
    private Long id;

    /**
     * 删除标识：1是；0否
     */
    @Schema(name = "删除标识：1是；0否")
    @TableField("DELETED")
    private BigDecimal deleted;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("CREATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    @TableField("UPDATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    @TableField("CREATER")
    private String creater;

    /**
     * 创建人ID
     */
    @Schema(name = "创建人ID")
    @TableField("CREATERID")
    private Long createrId;

}
