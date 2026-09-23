package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_COLLABORATIVE_PROJECT")
public class CollaborativeProject implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String projectId;
    private String projectName;
    private String collaborationType;
    private String projectStatus;
    private Integer participantCount;
    private Integer progress;
    private String departments;
    private String description;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private Integer delFlag;

    public void setProjectId(String projectId) { this.projectId = projectId; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public void setDelFlag(int delFlag) { this.delFlag = delFlag; }
    public void setDelFlag(Integer delFlag) { this.delFlag = delFlag; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public void setProjectStatus(String projectStatus) { this.projectStatus = projectStatus; }
}
