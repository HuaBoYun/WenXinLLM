package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TblYyUserQuery entity. @author MyEclipse Persistence Tools
 * 用户 使用望远镜 查询记录表
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YY_USER_QUERY")
@Schema(name="TblYyUserQueryMySql对象")
public class TblYyUserQueryMySql implements java.io.Serializable {
    private static final long serialVersionUID = 8990869363668729766L;
    @TableId("RECORDID")
    private BigDecimal recordid; //查询记录
    @TableField("QUERYSTAFF")
    private BigDecimal querystaff;
    @TableField("ORGID")
    private BigDecimal orgid;
    @TableField("QUERYTIME")
    private Date querytime;  //查询时间
    @TableField("PAYMONEY")
    private Double paymoney;       //查询支付时间
    @TableField("REPORTNAME")
    private String reportName;
    //private Set<TblYyQueryPrice> tblYyQueryPrices = new HashSet<TblYyQueryPrice>(0);
    @TableField("STAFFID")
    private BigDecimal staffid;
    @TableField("STAFFNAME")
    private String staffname;

    @Transient
    private TblStaffMySql tblStaff;  // 查询用户
    @Transient
    private TblOrganizationMySql tblOrganization;  // 所属组织


}