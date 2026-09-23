package com.huabo.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2021-10-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_CSWL_MANAGEMENT")
@Schema(name="TblCswlManagement对象")
public class TblCswlManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    public final static Integer STATE_SP = 1;//审批中
    public final static Integer STATE_TZ = 2;//调整
    public final static Integer STATE_TG = 3;//已通过
    public final static Integer STATE_ZZ = 4;//已终止
    public final static Integer STATE_GZ = 5;//已跟踪
    public final static Integer STATE_WC = 6;//已完成
    
    public final static Integer STATE_SC = 21;//审查中
    public final static Integer STATE_SCTZ = 22;//审查调整中
    public final static Integer STATE_SCWC = 23;//审查调整完成
    
      @TableId(value = "ASSETID" , type = IdType.INPUT)
      private BigDecimal assetid;

    @TableField("PROJECT_MANAGE")
    private String projectManage;

    @TableField("FILL_DATE")
    private LocalDateTime fillDate;

    @TableField("AW_SBYYB")
    private String awSbyyb;

    @TableField("AW_YYBJL")
    private String awYybjl;

    @TableField("AW_KHMC")
    private String awKhmc;

    @TableField("AW_KHSF")
    private String awKhsf;

    @TableField("AW_GDPM")
    private String awGdpm;

    @TableField("AW_GFTGQS")
    private String awGftgqs;

    @TableField("AW_NZYGFMC")
    private String awNzygfmc;

    @TableField("AW_GPDM")
    private String awGpdm;

    @TableField("AW_KHZCGSL")
    private BigDecimal awKhzcgsl;

    @TableField("AW_QZLTGSL")
    private BigDecimal awQzltgsl;

    @TableField("AW_XSGSL")
    private BigDecimal awXsgsl;

    @TableField("AW_SSGCZGB")
    private String awSsgczgb;

    @TableField("AW_BDZQSSSJ")
    private LocalDateTime awBdzqsssj;

    @TableField("AW_KHCGZZGBB")
    private String awKhcgzzgbb;

    @TableField("AW_GDZYGFSJ")
    private LocalDateTime awGdzygfsj;

    @TableField("AW_XSGJJRQ")
    private LocalDateTime awXsgjjrq;

    @TableField("COMMPYID")
    private String commpyid;

    @TableField("PROCESSID")
    private String processid;

    @TableField("PROCESSVERSION")
    private String processversion;

    @TableField("FXEXAM")
    private String fxexam;

    @TableField("KZJZEXAM")
    private String kzjzexam;

    @TableField("CATEID")
    private BigDecimal cateid;

    @TableField("STATE")
    private String state;

    @TableField("FLOWID")
    private BigDecimal flowid;

    @TableField("CWGLCODE")
    private String cwglcode;

    @TableField("CREATEUSERID")
    private String createuserid;


}
