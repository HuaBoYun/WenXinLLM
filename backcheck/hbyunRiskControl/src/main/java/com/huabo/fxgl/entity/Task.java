package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-08
 */
@Data
@ToString
@TableName("TBL_TASK")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String START = "0";
    public static final String END = "1";

    @TableId(type = IdType.INPUT)
	@Schema(name="主键ID")
    private BigDecimal taskid;

	@Schema
    private String taskname;

	@Schema
    private String taskcode;

	@Schema
    private String url;

	@Schema(name="创建人ID")
    private BigDecimal staffid;

	@Schema
    private LocalDateTime tasktime;

	@Schema
    private String status;

    public BigDecimal getTaskid() {
        return taskid;
    }

    public void setTaskid(BigDecimal taskid) {
        this.taskid = taskid;
    }
    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }
    public String getTaskcode() {
        return taskcode;
    }

    public void setTaskcode(String taskcode) {
        this.taskcode = taskcode;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public BigDecimal getStaffid() {
        return staffid;
    }

    public void setStaffid(BigDecimal staffid) {
        this.staffid = staffid;
    }
    public LocalDateTime getTasktime() {
        return tasktime;
    }

    public void setTasktime(LocalDateTime tasktime) {
        this.tasktime = tasktime;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
            "taskid=" + taskid +
            ", taskname=" + taskname +
            ", taskcode=" + taskcode +
            ", url=" + url +
            ", staffid=" + staffid +
            ", tasktime=" + tasktime +
            ", status=" + status +
        "}";
    }
}
