package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ORG_NO")
@Schema(name="TblOrgNo")
public class TblOrgNoMySql implements java.io.Serializable {

    private static final long serialVersionUID = 7985409817430444480L;
    @TableId("ORGID")
    private BigDecimal orgid;
    @TableField("NOID")
    private BigDecimal noid;
    @TableField("NOCODE")
    private String nocode;
    @TableField("NOSEPARTOR")
    private String noSepartor;
    @TableField("NONUMBER")
    private Integer noNumber;
    @TableField("ISUSEDEFAULT")
    private Integer isusedefault;
    @TableField("NOSUFFIX")
    private Integer noSuffix;
    @TableField("NEWNUMBER")
    private Integer newnumber;

    @Transient
    private String noname;

    @Transient
    private TblOrgNoIdMySql id;
    @Transient
    private TblOrganizationMySql tblOrganizationMySql;
    @Transient
    private TblAutonoInfoMySql tblAutonoInfoMySql;

    public TblOrgNoMySql() {
    }

    public TblOrgNoMySql(TblOrgNoIdMySql id) {
        this.id = id;
    }

    public TblOrgNoMySql(TblOrgNoIdMySql id, TblOrganizationMySql tblOrganizationMySql,
                         TblAutonoInfoMySql tblAutonoInfoMySql) {
        this.id = id;
        this.tblOrganizationMySql = tblOrganizationMySql;
        this.tblAutonoInfoMySql = tblAutonoInfoMySql;
    }

}