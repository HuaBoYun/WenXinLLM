package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Data
@ToString
@TableName("TBL_RISK_RECTIFYSOLUTION")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class RiskRectifysolution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键ID")
    @TableId(type = IdType.INPUT)
    private BigDecimal rectsolid;

    @TableField(exist = false)
    private Riskevent Riskevent;

	@Schema(name="方案编号")
    private String solutioncode;

	@Schema(name="方案名称")
    private String solutionname;

	@Schema
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startdate;

	@Schema
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date enddate;

	@Schema(name="参与人数")
    private BigDecimal participantnum;

	@Schema(name="被整改部门")
    private String rectifydepart;

	@Schema
    private String rectifyhead;

	@Schema
    private String suggestion;

	@Schema
    private String bugs;

	@Schema(name="录入人")
    private String recorder;

	@Schema
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date recorddate;

	@Schema
    private String memo;
	
	@Schema(name="事件编号")
	private BigDecimal riseveid;
//    private Set RiskRecsolAtt = new HashSet(0);
//    private Set<TblAttachment> tblAttachments = new HashSet<TblAttachment>();
//    public Set<TblAttachment> getTblAttachments() {
//        return tblAttachments;
//    }

   // public void setTblAttachments(Set<TblAttachment> tblAttachments) {
   //     this.tblAttachments = tblAttachments;
   // }

    public RiskRectifysolution() {
    }

    /** full constructor */
    public RiskRectifysolution(Riskevent Riskevent,
                                  String solutioncode, String solutionname, Date startdate,
                                  Date enddate, BigDecimal participantnum, String rectifydepart,
                                  String rectifyhead, String suggestion, String bugs,
                                  String recorder, Date recorddate, String memo, Set tblRiskRecsolAtts) {
        this.Riskevent = Riskevent;
        this.solutioncode = solutioncode;
        this.solutionname = solutionname;
        this.startdate = startdate;
        this.enddate = enddate;
        this.participantnum = participantnum;
        this.rectifydepart = rectifydepart;
        this.rectifyhead = rectifyhead;
        this.suggestion = suggestion;
        this.bugs = bugs;
        this.recorder = recorder;
        this.recorddate = recorddate;
        this.memo = memo;
        this.riseveid = riseveid;
    }

    // Property accessors
    public BigDecimal getRectsolid() {
        return this.rectsolid;
    }
    }
