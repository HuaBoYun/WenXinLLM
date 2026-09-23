package com.huabo.compliance.mysql.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AUDIT_OPTION")
@Schema(name="TblAuditOption对象")
public class TblAuditOptionMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("OPT_ID")
    private BigDecimal optId;

    @TableField("OPT_DESC")
    private String optDesc;

    @TableField("OPT_STAFFID")
    private BigDecimal optStaffid;

    @TableField("RELATION_ID")
    private BigDecimal relationId;

    @TableField("OPT_STATE")
    private String optState;

    @TableField("CREATE_DATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @TableField("CYID")
    private BigDecimal cyid;

    @TableField("staffidName")
    private String staffidName;

}
