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
 * @author zf
 * @since 2024-04-03
 */
@Getter
@Setter
@TableName("TBL_ZSGX_LABEL")
@Schema(name="TblZsgxLabel对象")
public class TblZsgxLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("LABEL")
    private String label;

    @TableField("VALUE")
    private String value;

    @TableField("SORT")
    private Integer sort;

    @TableField("SELECTED")
    private Integer selected;

    @TableField("PARENT_VALUE")
    private String parentValue;

    @TableField("TYPE_VALUE")
    private String typeValue;

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

    @TableField("PID")
    private String pid;


}
