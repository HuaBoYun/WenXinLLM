package com.huabo.etl.domain;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName : KettleTransLog
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2022-12-03 15:55:31
 */
@Schema
@TableName("kettle_trans_log")
public class KettleTransLog implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @Schema(description="null")
    private Integer idBatch;
    /**
     *
     */
    @Schema(description="null")
    private String channelId;
    /**
     *
     */
    @Schema(description="null")
    private String transname;
    /**
     *
     */
    @Schema(description="null")
    private String status;
    /**
     *
     */
    @Schema(description="null")
    private Long linesRead;
    /**
     *
     */
    @Schema(description="null")
    private Long linesWritten;
    /**
     *
     */
    @Schema(description="null")
    private Long linesUpdated;
    /**
     *
     */
    @Schema(description="null")
    private Long linesInput;
    /**
     *
     */
    @Schema(description="null")
    private Long linesOutput;
    /**
     *
     */
    @Schema(description="null")
    private Long linesRejected;
    /**
     *
     */
    @Schema(description="null")
    private Long errors;
    /**
     *
     */
    @Schema(description="null")
    private Date startdate;
    /**
     *
     */
    @Schema(description="null")
    private Date enddate;
    /**
     *
     */
    @Schema(description="null")
    private Date logdate;
    /**
     *
     */
    @Schema(description="null")
    private Date depdate;
    /**
     *
     */
    @Schema(description="null")
    private Date replaydate;
    /**
     *
     */
    @Schema(description="null")
    private String logField;

    /**
     *
     */
    public Integer getIdBatch() {
        return this.idBatch;
    }

    /**
     *
     */
    public void setIdBatch(Integer idBatch) {
        this.idBatch = idBatch;
    }

    /**
     *
     */
    public String getChannelId() {
        return this.channelId;
    }

    /**
     *
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     *
     */
    public String getTransname() {
        return this.transname;
    }

    /**
     *
     */
    public void setTransname(String transname) {
        this.transname = transname;
    }

    /**
     *
     */
    public String getStatus() {
        return this.status;
    }

    /**
     *
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     */
    public Long getLinesRead() {
        return this.linesRead;
    }

    /**
     *
     */
    public void setLinesRead(Long linesRead) {
        this.linesRead = linesRead;
    }

    /**
     *
     */
    public Long getLinesWritten() {
        return this.linesWritten;
    }

    /**
     *
     */
    public void setLinesWritten(Long linesWritten) {
        this.linesWritten = linesWritten;
    }

    /**
     *
     */
    public Long getLinesUpdated() {
        return this.linesUpdated;
    }

    /**
     *
     */
    public void setLinesUpdated(Long linesUpdated) {
        this.linesUpdated = linesUpdated;
    }

    /**
     *
     */
    public Long getLinesInput() {
        return this.linesInput;
    }

    /**
     *
     */
    public void setLinesInput(Long linesInput) {
        this.linesInput = linesInput;
    }

    /**
     *
     */
    public Long getLinesOutput() {
        return this.linesOutput;
    }

    /**
     *
     */
    public void setLinesOutput(Long linesOutput) {
        this.linesOutput = linesOutput;
    }

    /**
     *
     */
    public Long getLinesRejected() {
        return this.linesRejected;
    }

    /**
     *
     */
    public void setLinesRejected(Long linesRejected) {
        this.linesRejected = linesRejected;
    }

    /**
     *
     */
    public Long getErrors() {
        return this.errors;
    }

    /**
     *
     */
    public void setErrors(Long errors) {
        this.errors = errors;
    }

    /**
     *
     */
    public Date getStartdate() {
        return this.startdate;
    }

    public String getStartdateStr() {
        return DateUtil.formatDate(this.startdate);
    }

    /**
     *
     */
    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    /**
     *
     */
    public Date getEnddate() {
        return this.enddate;
    }

    public String getEnddateStr() {
        return DateUtil.formatDate(this.enddate);
    }
    /**
     *
     */
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    /**
     *
     */
    public Date getLogdate() {
        return this.logdate;
    }

    /**
     *
     */
    public void setLogdate(Date logdate) {
        this.logdate = logdate;
    }

    /**
     *
     */
    public Date getDepdate() {
        return this.depdate;
    }

    /**
     *
     */
    public void setDepdate(Date depdate) {
        this.depdate = depdate;
    }

    /**
     *
     */
    public Date getReplaydate() {
        return this.replaydate;
    }

    /**
     *
     */
    public void setReplaydate(Date replaydate) {
        this.replaydate = replaydate;
    }

    /**
     *
     */
    public String getLogField() {
        return this.logField;
    }

    /**
     *
     */
    public void setLogField(String logField) {
        this.logField = logField;
    }

    @Override
    public String toString() {
        return "KettleTransLog{" +
                "idBatch=" + idBatch +
                ", channelId='" + channelId + '\'' +
                ", transname='" + transname + '\'' +
                ", status='" + status + '\'' +
                ", linesRead=" + linesRead +
                ", linesWritten=" + linesWritten +
                ", linesUpdated=" + linesUpdated +
                ", linesInput=" + linesInput +
                ", linesOutput=" + linesOutput +
                ", linesRejected=" + linesRejected +
                ", errors=" + errors +
                ", startdate=" + startdate +
                ", enddate=" + enddate +
                ", logdate=" + logdate +
                ", depdate=" + depdate +
                ", replaydate=" + replaydate +
                ", logField='" + logField + '\'' +
                '}';
    }


}