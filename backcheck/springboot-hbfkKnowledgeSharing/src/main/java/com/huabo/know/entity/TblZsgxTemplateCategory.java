package com.huabo.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
@Getter
@Setter
@TableName("TBL_ZSGX_TEMPLATE_CATEGORY")
@Schema(name="TblZsgxTemplateCategory对象")
public class TblZsgxTemplateCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("NAME")
    private String name;

    @TableField("CODE")
    private String code;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("PID")
    private String pid;

    @TableField("TEMPLATES")
    private String templates;

    @TableField("SORT")
    private Integer sort;

    @TableField("CATEGORY_LEVEL")
    private Integer categoryLevel;

    @TableField("CREATE_COMPANY")
    private String createCompany;

    @TableField("CREATE_DEPT")
    private String createDept;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("DELETED")
    private Integer deleted;

    @TableField("PARENT_NAMES")
    private String parentNames;


}
