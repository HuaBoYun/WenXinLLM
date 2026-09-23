package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 审批流程开关
 *
 * @Description: TODO
 * @author: songxiangying
 * @date 2017年2月13日 上午10:32:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_PROCESS_SETTING")
@Schema(name="ProcessSetting")
public class ProcessSettingMySql implements Serializable {

    public final static String OFF = "OFF";//关闭
    public final static String ON = "ON";//开启

    private static final long serialVersionUID = 1L;

    @TableId("SETTINGID")
    @Id
    private BigDecimal settingId;
    @TableField("STATUS")
    private String status;//状态
    @TableField("MODULE")
    private String module; //模块名称
    @TableField("REMARK")
    private String remark;//备注
    @TableField("ORGID")
    private BigDecimal orgid;//备注
    @TableField("COMPANYID")
    private BigDecimal companyid;//备注

    @Transient
    private TblOrganizationMySql tblOrganizationMySql;
    @Transient
    private TblOrganizationMySql companyMySql;


}
