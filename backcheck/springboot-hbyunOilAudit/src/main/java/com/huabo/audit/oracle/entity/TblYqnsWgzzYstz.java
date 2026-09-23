package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.huabo.audit.oracle.entity.base.ReservedEntity;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_WGZZ_YSTZ")
@Data
@Schema(name="移送台账")
@Accessors(chain = true)
public class TblYqnsWgzzYstz extends ReservedEntity {
	
    @TableField(value = "ID")
    @Schema(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal id;
    
	@TableField("YJTIME")
    @Schema(name = "移交时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date yjtime;

    @TableField(value = "TZNUMBER")
    @Schema(name = "序号")
    private String tznumber;

    @TableField(value = "PROBLEMCLUE")
    @Schema(name = "问题线索")
    private String problemclue;
    
    @TableField("PROJECTID")
    @Schema(name = "审计项目")
    private BigDecimal projectid;
    
    //审计项目名称
    @Transient
    private String projectname;

    @TableField("STATUS")
    @Schema(name = "状态")
    private Integer status;

    @TableField("CREATOR")
    @Schema(name = "创建人")
    private BigDecimal creator;

    @Transient
    private String attIds;
    
    @TableField(value = "FILUE")
    @Schema(name = "附件")
    private String filue;

	@TableField(value = "IMPORGID")
	@Schema(name = "审计实施单位")
	private BigDecimal imporgid;
	
	//审计实施单位名称
	@Transient
    private String imporgname;
	
	@TableField("CHECKRESULT")
	@Schema(name = "核查结论")
	private String checkresult;
	
	@TableField("SITUATION")
	@Schema(name = "处理情况")
	private String situation;
	
	@TableField("MEMO")
	@Schema(name = "备注")
	private String memo;
	
}
