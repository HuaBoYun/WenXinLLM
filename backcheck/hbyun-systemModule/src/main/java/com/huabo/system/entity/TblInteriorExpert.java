package com.huabo.system.entity;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_INTERIOR_EXPERT")
@Schema(name="TblInteriorExpert")
public class TblInteriorExpert implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="INTERIORID",type = IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal interiorid;
    @TableField("QUALIFICATION")
    @Schema(name="资格证书")
    private String qualification;//资格证书
    @TableField("POSITION")
    @Schema(name="职务")
    private String position;//职务
    @TableField("PROFESSIONAL")
    @Schema(name="职称")
    private String professional;//职称
    @Transient
    private TblStaff staff;
    @Transient
    private TblOrganization organization;
    @TableField("ORGID")
    @Schema(name="隶属组织ID")
    private BigDecimal orgId;
    @TableField("STAFFID")
    @Schema(name="关联用户ID")
    private BigDecimal staffid;

    public TblInteriorExpert() {
    }
}
