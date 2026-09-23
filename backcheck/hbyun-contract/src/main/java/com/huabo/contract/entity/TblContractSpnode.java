package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-03-22
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_CONTRACT_SPNODE")
@Schema(name="TblContractSpnode对象")
public class TblContractSpnode implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "SPNODEID",type= IdType.INPUT)
      private BigDecimal spnodeid;

    @TableField("NODEID")
    private BigDecimal nodeid;

    @TableField("STARTDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startdate;

    @TableField("ENDDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date enddate;

    @TableField("NODESTATUS")
    private String nodestatus;

    @TableField("NODEMEMO")
    private String nodememo;

    @TableField("NODECONTENT")
    private String nodecontent;

    @TableField("NODEMONEY")
    private BigDecimal nodemoney;

    @TableField("NODEFINISHDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date nodefinishdate;

    @TableField("NODEPLANFINISHDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date nodeplanfinishdate;

    @TableField("FORMID")
    private Long formid;

    @TableField("CONTRACTID")
    private BigDecimal contractid;//

    @TableField("NODEPOST")
    private String nodepost;//收款比例

    @TableField("ISWY")
    private String iswy; //是否违约
    

    @Schema(name = "违约情况")
    private String wyContent;

    @Schema(name = "合同名称")
    private String contractname;

    @Schema(name = "合同编号")
    private String contractno;
    private String contracttype;
    private String recordtype;
    private BigDecimal flowid;
}
