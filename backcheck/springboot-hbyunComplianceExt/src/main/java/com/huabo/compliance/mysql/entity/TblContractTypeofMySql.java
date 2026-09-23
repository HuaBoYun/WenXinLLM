package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 合同类型维护表。
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_CONTRACT_TYPEOF")
@Schema(name="TblContractTypeof")
public class TblContractTypeofMySql implements Serializable {
    private static final long serialVersionUID = 6312252984564842416L;
    @TableId("TYPEID")
    private Integer typeId;
    @TableField("TYPENAME")
    private String typeName;
    @TableField("CREATESTAFF")
    private Integer createStaff;
    @TableField("CREATETIME")
    private Date createTime;
    @TableField("ORGID")
    private Integer orgId;
    @TableField("UPDATESTAFF")
    private Integer updateStaff;
    @TableField("UPDATETIME")
    private Date updateTime;
    @TableField("PAGEURL")
    private String pageUrl;
    @Transient
    private TblContractTypeofMySql fatherType;
    @Transient
    private List<TblContractTypeofMySql> childrenList = new ArrayList<TblContractTypeofMySql>(0);
    @TableField("SETTINGID")
    private String settingid;//流程id

    public TblContractTypeofMySql() {

    }

    public TblContractTypeofMySql(Integer typeId) {
        this.typeId = typeId;
    }


}
