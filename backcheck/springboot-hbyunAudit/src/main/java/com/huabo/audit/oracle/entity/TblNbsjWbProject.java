package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

@Data
@Schema(name="外部审计项目")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_NBSJ_WBPROJECT")
public class TblNbsjWbProject {
	
	@TableId("PROJECTID")
    @Id
    @Column(name = "PROJECTID")
    @Schema(name = "PROJECTID")
    private BigDecimal projectid;
	
	@TableField(value = "PROJECTNAME")
	@Column(name = "PROJECTNAME")
	@Schema(name = "项目名称")
	private String projectname;
	
    @TableField(value = "CREATESTAFF")
    @Column(name = "CREATESTAFF")
    @Schema(name="创建人id",hidden=true)
    private BigDecimal createstaff;

    
    @TableField(value = "PROJECTCODE")
    @Column(name = "PROJECTCODE")
    @Schema(name = "项目编号")
    private String projectcode;
    
    
    
    @TableField(value = "PROJECTTYPE")
    @Column(name = "PROJECTTYPE")
    @Schema(name = "3-非系统实施 or 外部 4.外部审计")
    private Integer projecttype;
    

    @Schema(name = "创建时间")
    @TableField("CRETETIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "CRETETIME")
    private Date createtime;
    
    @TableField(value = "LINKORGID")
    @Column(name = "LINKORGID")
    @Schema(name = "所属公司主键")
    private BigDecimal linkOrgId;
}
