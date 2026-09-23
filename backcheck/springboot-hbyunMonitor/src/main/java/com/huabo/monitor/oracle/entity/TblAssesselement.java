package com.huabo.monitor.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 评价要素
 *
 * @author SongXiangYing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("TBL_ASSESSELEMENT")
public class TblAssesselement implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal asseleid;
    private String elementNumber; //要素编号
    private String elementname;  //要素名称
    private double standardscore; //标准分数
    private String memo;  //备注
    private String businesstype; //业务类别
    private String businessattribute; //业务属性
    private String auditpoint;   //审查要点
    private double assessrules; //评分规则
    private String status;   //状态
    private String tblComany; //公司
}