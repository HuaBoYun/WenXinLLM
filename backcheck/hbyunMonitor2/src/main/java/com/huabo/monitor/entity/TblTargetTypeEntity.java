package com.huabo.monitor.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@TableName("TBL_NBSJ_TARGETTYPE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblTargetTypeEntity {

	public static final Integer TEMP_NUMBER=0;//审计模板
	public static final Integer ZY_NUMBER=1;//审计指引

	private static final long serialVersionUID = 1L;
	
	@TableId(value = "targetId", type= IdType.AUTO)
	@Schema
	private BigDecimal targetId;
	
	@TableField(value = "targetName")
	@Schema
	private String targetName;
	
	@TableField(value = "rulename")
	@Schema
	private String targetDesc;
	
	@TableField(value = "PARENTID")
	@Schema
	private BigDecimal parentId;
	
	@TableField(value = "CREATETIME")
	@Schema
	@JSONField(format = "yyyy-MM-dd HH:mm:dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:dd")
	private Date createTime;
	
	@TableField(value = "UPDATETIME")
	@Schema
	@JSONField(format = "yyyy-MM-dd HH:mm:dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:dd")
	private Date updateTime;
	
	@Schema
	@Transient
	private TblNbsjTempleteEntity nbsjTemplete;
	
	@TableField(value = "STATUS")
	@Schema
	private Integer status;
	
	@Schema
	@Transient
	private Integer finshCount;
	
	@Schema
	@Transient
	private Integer sumCount;
	
	@Schema
	@Transient
	private Integer sheetCount;
	
	@Schema
	@Transient
	private Integer unFinshCount;
	
}
