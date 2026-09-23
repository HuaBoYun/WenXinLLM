package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_REPORT_SUBSCRIBER")
public class ReportSubscriber implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String subscriberId;
    private String reportId;
    private String userName;
    private String department;
    private String email;
    private String deliveryMethod;
    private String status;
    private Date subscribeTime;
    private Date createTime;
    private Date updateTime;
    private Integer delFlag;
}
