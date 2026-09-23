package com.huabo.monitor.oracle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * 评价分类
 *
 * @author SongXiangYing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblAssesscategory implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal asscatid;
    private TblAssesstemple tblAssesstemple;
    private String catname;
    private double catweight;
    private String memo;
    private String catDes;
    private BigDecimal fatherasscatid;
    private Set<TblAssesscategory> children = new HashSet<TblAssesscategory>();
    private Set<TblAssEleCategory> assEleCategories = new HashSet<TblAssEleCategory>();
}