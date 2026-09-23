package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_COLLAB_PARTICIPANT")
public class CollabParticipant implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String participantId;
    private String projectId;
    private String userName;
    private String department;
    private String role;
    private Integer contribution;
    private Date lastActive;
    private String status;
    private Date createTime;
    private Integer delFlag;
}
