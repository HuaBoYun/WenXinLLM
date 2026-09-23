package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

 
@Data
@TableName("TBL_CONTROL_ENTRIES")
@Schema(name="一体化管控关联管控条目表")
public class TblControlEntries  implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Schema(name = "主键")
    @TableId(value = "ID")
    @Id
    private BigDecimal id;
    
    @Schema(name = "关联一体化管控措施ID")
    @TableField(value="CONMATID")
    private BigDecimal conmatid;
    
    @Schema(name = "具体管控措施")
    @TableField(value="FIELD1")
    private String field1;
    
    @Schema(name = "预计完成时间")
    @TableField(value = "FIELD2")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date field2;
    
    @Schema(name = "责任人")
    @TableField(value="FIELD3")
    private String field3;
    
    @Schema(name = "责任人id")
    @TableField(value="FIELD13")
    private BigDecimal field13;
    
    @Schema(name = "有限公司责任领导")
    @TableField(value="FIELD6")
    private String field6;
    
    @Schema(name = "有限公司责任领导id")
    @TableField(value="FIELD14")
    private String field14 ;
    
    @Schema(name = "配合单位或部门")
    @TableField(value="FIELD7")
    private String field7 ;

    @Schema(name = "配合单位或部门id")
    @TableField(value="FIELD15")
    private String field15;
    
    @Schema(name = "是否完成：是 否")
    @TableField(value="FIELD4")
    private String field4;
    
    @Schema(name = "管控措施是否逾期")
    @TableField(value="FIELD11")
    private String field11;
    
    @Schema(name = "措施完成时间")
    @TableField(value = "FIELD5")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date field5;
    
    @Schema(name = "本月风险管控措施及实施情况")
    @TableField(value="FIELD10")
    private String field10;
    
    @Schema(name = "下月风险管控措施管控措施")
    @TableField(value="FIELD12")
    private String field12;
    
    @Schema(name = "停止时间")
    @TableField(value = "STOPTIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date stopTime;
    
    @Schema(name = "所属部门")
    @TableField("LINKDEPTID")
    @Column(name = "LINKDEPTID")
    private BigDecimal linkDeptId;
    
    @Schema(name = "所属公司")
    @TableField("LINKORGID")
    @Column(name = "LINKORGID")
    private BigDecimal linkOrgId;
    
    @Schema(name = "创建人")
    @TableField("CREATOR")
    @Column(name = "CREATOR")
    private BigDecimal creator;
    
    @Schema(name = "标记旧数据")
    @TableField("MARK")
    @Column(name = "MARK")
    private String mark;
    
    
    @Schema(name = "创建时间")
    @TableField(value = "CREATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    
    
    @Schema(name = "责任人所在部门ID")
    @TableField(exist=false)
    private BigDecimal field3DeptId;
    
    @Schema(name = "责任人所在部门名称")
    @TableField(exist=false)
    private String field3DeptName;

    public TblControlEntries(){
    	
    }
    
	public TblControlEntries(BigDecimal id, BigDecimal conmatid, String field1, Date field2, String field3,
			BigDecimal field13, String field6, String field14, String field7, String field15, String field4,
			String field11, Date field5, String field10, String field12, String mark) {
		super();
		this.id = id;
		this.conmatid = conmatid;
		this.field1 = field1;
		this.field2 = field2;
		this.field3 = field3;
		this.field13 = field13;
		this.field6 = field6;
		this.field14 = field14;
		this.field7 = field7;
		this.field15 = field15;
		this.field4 = field4;
		this.field11 = field11;
		this.field5 = field5;
		this.field10 = field10;
		this.field12 = field12;
		this.mark = mark;
	}

 
 
    
    
}
     

