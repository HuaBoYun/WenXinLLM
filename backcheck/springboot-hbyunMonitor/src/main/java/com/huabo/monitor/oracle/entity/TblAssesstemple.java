package com.huabo.monitor.oracle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * 评价模板
 *
 * @author SongXiangYing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblAssesstemple implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal asstemid;//主键（自增）
    private String templeNumber;//模板编号
    private String templename;//模板名称
    private String memo;//备注
    private TblStaff staff;//用户
    private Date modifyDateTime;//修改时间
    private Integer templeStatus;//模板状态
    private String templeDesc;//模板说明
    private Set<TblOrganization> organizations = new HashSet<TblOrganization>();
    //评价分类
    private Set<TblAssesscategory> tblAssesscategories = new HashSet<TblAssesscategory>();
    private Set<TblAssess> tblAssesses = new HashSet<TblAssess>();
    private String reorg;
    private String reorgText;
    private TblOrganization tblComany;
}