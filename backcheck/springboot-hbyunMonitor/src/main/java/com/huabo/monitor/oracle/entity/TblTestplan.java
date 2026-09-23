package com.huabo.monitor.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_TESTPLAN")
public class TblTestplan implements Serializable {

    private BigDecimal testplanid;//测试计划id
    private String plannumber;//计划编号
    private String planname;//计划名称
    private String planyear;//计划年度
    private String testtype;//测试类型
    private String planmadeorg;//
    private String planmadedep;//计划制定部门
    private Date starttime;//计划开始时间
    private Date endtime;//计划结束时间
    private String planleader;//负责人
    private double planfee;//开展费用（数字）
    private String numberofpeople;//投入人力（数字）
    private String testedorgs;//被测试机构
    private String memo;
    private String planstatus;//计划状态：未启动、已启动、执行中、已完成
    private Integer orgid;//计划制定部门id
    private Integer staffid;//负责人
    private Integer creatid;//创建人
    private Integer returnstatus ;//退回状态 1 退回
    private TblTestTemplate template;//测试模板
    private Set tblTestplanMatrixes = new HashSet(0);
    private Set tblAttachments = new HashSet(0);

}
