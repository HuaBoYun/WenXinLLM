package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_Announcement_Management")
@Schema(name="公告维护实体类")
public class TBLAnnouncementManagement {
    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.INPUT)
    @Schema(name = "公告维护ID")
    @TableField("ID")
    private BigDecimal id;
    @Schema(name = "公告维护标题")
    @TableField("TITLE")
    private String title;
    @Schema(name = "公告维护内容")
    @TableField("ACONTENT")
    private String acontent;
    @Schema(name = "失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("EXPIRATION_TIME")
    private Date expirationTime;
    @Schema(name = "创建人ID")
    @TableField("STAFFID")
    private BigDecimal staffid;
    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;
    @Schema(name = "逻辑删除，1已删除，0未删除")
    @TableField("IS_DELETE")
    private Integer isDelete;
    @Schema(name = "状态")
    @TableField("STATUS")
    private Integer status;
    @Schema(name = "用户名")
    @TableField("STAFFNAME")
    private String staffName;


    @Schema(name = "是否有效")
    @TableField(exist=false)
    private String isValid;
}
