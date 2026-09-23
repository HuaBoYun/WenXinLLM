package com.huabo.monitor.mysql.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SYSTEM_MODULE")
@Schema(name="TblSystemModuleMySql")
public class TblSystemModuleMySql implements Serializable {

    private static final long serialVersionUID = -1107456731364079572L;

    @TableId("MODELID")
    private Integer modelId;
    @TableField("MODELNO")
    private String modelNo;
    @TableField("MODELNAME")
    private String modelName;
    @TableField("MODELURL")
    private String modelUrl;
    @TableField("MODELSTATUS")
    private Integer modelStatus;//0未启动   1.已启动  2.已弃用
    @TableField("MODELTYPE")
    private String modelType;
    @TableField("CREATETIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    @TableField("MODIFYTIME")
    private Date modifyTime;
    @TableField("MODELORG")
    private BigDecimal modelOrg;
    @TableField("CREATEPERSON")
    private BigDecimal createPerson;
    @TableField("MODIFYPERSON")
    private BigDecimal modifyPerson;
    @TableField("MODELORDER")
    private Date modelorder;

    @Transient
    private String createPersonName;
    @Transient
    private TblStaffMySql staff;
    @Transient
    private TblOrganizationMySql organization;

    @Transient
    private List<TblSystemModelFlowMySql> tblSystemModelFlowList = new ArrayList<TblSystemModelFlowMySql>(0);
    @Transient
    private Set<TblOrganizationMySql> tblOrgSet = new HashSet<TblOrganizationMySql>(0);
    @Transient
    private List<TblFlowMySql> flowList = new ArrayList<TblFlowMySql>(0);

}
