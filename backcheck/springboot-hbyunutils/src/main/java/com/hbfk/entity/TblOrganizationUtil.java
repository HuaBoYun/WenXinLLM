package com.hbfk.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;


/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
@Data
@ToString
public class TblOrganizationUtil implements Serializable {

    private static final long serialVersionUID = 1L;
        private BigDecimal orgid; //组织ID

        private String orgname; //组织的名称

        private BigDecimal fatherorgid; //上级组织（父组织）的ID

        private String orgnumber; //组织的编号

        private String orgmeno; // 组织成员的名称

        private String memo; //备注

        private String icode; //组织编码

        private Integer orgtype; //

        private Integer audittype; //审计类型
        private Integer status; //组织的状态

        private String iszy; //

        private String hyzsktype; //

        private Integer orderid; //订单ID

        private Integer outsideid; //外部ID

        private String outsideopendid; // 外部开放ID

        private Integer isautonumber; //是否自动编号

        private Date orgcreate; //组织创建日期

        private Integer isinitialization; //是否初始化

        private String duties; //职责

        private Integer industryid; //行业ID

        private String bywx;

        private String datasource;

        private String historycode; //数据源

        private String historydepartmentid;
        
        private String writtenByDept;
        
        @Schema(description = "是否使用密级 0 不使用；1 使用")
        private Integer useSecrect;
}
