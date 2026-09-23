package com.huabo.compliance.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_INTERIOR_EXPERT")
@Schema(name="TblInteriorExpert")
public class TblInteriorExpert implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("INTERIORID")
    @Column(name="INTERIORID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal interiorid;
    @TableField("QUALIFICATION")
    @Column(name="QUALIFICATION")
    private String qualification;//资格证书
    @TableField("POSITION")
    @Column(name="POSITION")
    private String position;//职务
    @TableField("PROFESSIONAL")
    @Column(name="PROFESSIONAL")
    private String professional;//职称
    @Transient
    private TblStaff staff;
    @Transient
    private TblOrganization organization;
    @TableField("ORGID")
    @Column(name="ORGID")
    private BigDecimal orgId;
    @TableField("STAFFID")
    @Column(name="STAFFID")
    private BigDecimal staffid;

    public TblInteriorExpert() {
    }
}
