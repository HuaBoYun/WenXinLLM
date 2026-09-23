package com.huabo.monitor.oracle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 要素和评分类别中间表
 *
 * @author SongXiangYing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblAssEleCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal elementGategoryId;
    private TblAssesselement assesselement;
    private TblAssesscategory assesscategory;
    //标准分
    private Double standardScore = 5d;
}