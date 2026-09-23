package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_COLLAB_ACTIVITY")
public class CollabActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String activityId;
    private String projectId;
    private String title;
    private String description;
    private String type;
    private String userName;
    private String department;
    private Date activityTime;
    private Date createTime;
    private Integer delFlag;
}
